package com.musicia.servlet;

import com.musicia.dao.SongDAO;
import com.musicia.model.Song;
import com.musicia.model.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SongServlet extends HttpServlet {
    private SongDAO songDAO = new SongDAO();
    private static final String UPLOAD_DIRECTORY = "uploads";

    // Add CORS headers to all responses
    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setCorsHeaders(response);
        
        // Check if user is logged in and is an artist
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !user.isArtist()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            JSONObject err = new JSONObject();
            err.put("success", false);
            err.put("message", "Unauthorized");
            response.getWriter().write(err.toString());
            return;
        }

        try {
            // Create the upload directory if it doesn't exist
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            // Process the uploaded files
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            
            List<FileItem> items = upload.parseRequest(request);
            
            String title = null;
            String genre = null;
            String songFilePath = null;
            String coverArtPath = null;

            for (FileItem item : items) {
                if (item.isFormField()) {
                    if ("title".equals(item.getFieldName())) {
                        title = item.getString();
                    } else if ("genre".equals(item.getFieldName())) {
                        genre = item.getString();
                    }
                } else {
                    String fileName = new File(item.getName()).getName();
                    String filePath = uploadPath + File.separator + fileName;
                    File storeFile = new File(filePath);
                    item.write(storeFile);

                    if (item.getFieldName().equals("song")) {
                        songFilePath = UPLOAD_DIRECTORY + "/" + fileName;
                    } else if (item.getFieldName().equals("coverArt")) {
                        coverArtPath = UPLOAD_DIRECTORY + "/" + fileName;
                    }
                }
            }

            // Save song information to database
            Song song = new Song(title, user.getId(), songFilePath, coverArtPath, genre);
            songDAO.addSong(song);

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", true);
            jsonResponse.put("message", "Song uploaded successfully");
            
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            JSONObject err = new JSONObject();
            err.put("success", false);
            err.put("message", "Error: " + e.getMessage());
            response.getWriter().write(err.toString());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null || !user.isArtist()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                JSONObject err = new JSONObject();
                err.put("success", false);
                err.put("message", "Unauthorized");
                response.getWriter().write(err.toString());
                return;
            }

            String pathInfo = request.getPathInfo(); // /{id}
            if (pathInfo == null || pathInfo.length() <= 1) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int id = Integer.parseInt(pathInfo.substring(1));
            Song song = songDAO.getSongById(id);
            if (song == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if (song.getArtistId() != user.getId()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                JSONObject err = new JSONObject();
                err.put("success", false);
                err.put("message", "Forbidden");
                response.getWriter().write(err.toString());
                return;
            }

            // delete file from disk if present
            String fp = song.getFilePath();
            if (fp != null) {
                String fname = fp.replace('\\','/');
                int idx = fname.lastIndexOf('/');
                if (idx >= 0) fname = fname.substring(idx+1);
                String realPath = getServletContext().getRealPath(UPLOAD_DIRECTORY) + File.separator + fname;
                File f = new File(realPath);
                if (f.exists()) f.delete();
            }

            songDAO.deleteSong(id);

            JSONObject ok = new JSONObject();
            ok.put("success", true);
            ok.put("message", "Deleted");
            response.setContentType("application/json");
            response.getWriter().write(ok.toString());

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            JSONObject err = new JSONObject();
            err.put("success", false);
            err.put("message", "Error: " + e.getMessage());
            response.getWriter().write(err.toString());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null || !user.isArtist()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                JSONObject err = new JSONObject();
                err.put("success", false);
                err.put("message", "Unauthorized");
                response.getWriter().write(err.toString());
                return;
            }

            String pathInfo = request.getPathInfo(); // /{id}
            if (pathInfo == null || pathInfo.length() <= 1) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int id = Integer.parseInt(pathInfo.substring(1));
            Song song = songDAO.getSongById(id);
            if (song == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if (song.getArtistId() != user.getId()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                JSONObject err = new JSONObject();
                err.put("success", false);
                err.put("message", "Forbidden");
                response.getWriter().write(err.toString());
                return;
            }

            // Read form-encoded body for simple metadata update
            String body = request.getReader().lines().reduce("", (a,b) -> a + b);
            java.util.Map<String,String> params = new java.util.HashMap<>();
            for (String pair : body.split("&")) {
                String[] kv = pair.split("=");
                if (kv.length == 2) params.put(java.net.URLDecoder.decode(kv[0], "UTF-8"), java.net.URLDecoder.decode(kv[1], "UTF-8"));
            }

            String title = params.getOrDefault("title", song.getTitle());
            String genre = params.getOrDefault("genre", song.getGenre());
            String coverArt = params.getOrDefault("coverArt", song.getCoverArt());

            songDAO.updateSongInfo(id, title, genre, coverArt);

            JSONObject ok = new JSONObject();
            ok.put("success", true);
            ok.put("message", "Updated");
            response.setContentType("application/json");
            response.getWriter().write(ok.toString());

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            JSONObject err = new JSONObject();
            err.put("success", false);
            err.put("message", "Error: " + e.getMessage());
            response.getWriter().write(err.toString());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setCorsHeaders(response);
        
        try {
            String pathInfo = request.getPathInfo();
            List<Song> songs;

            if (pathInfo == null || "/".equals(pathInfo)) {
                songs = songDAO.getAllSongs();
            } else {
                int artistId = Integer.parseInt(pathInfo.substring(1));
                songs = songDAO.getSongsByArtist(artistId);
            }

            JSONArray jsonSongs = new JSONArray();
            for (Song song : songs) {
                // Build a URL that points to the streaming servlet for the uploaded file
                String filePath = song.getFilePath();
                String fileUrl = filePath;
                if (filePath != null && !filePath.isEmpty()) {
                    // normalize and extract filename
                    String fname = filePath.replace('\\', '/');
                    int idx = fname.lastIndexOf('/');
                    if (idx >= 0) fname = fname.substring(idx + 1);
                    // URL encode the filename
                    String enc = java.net.URLEncoder.encode(fname, java.nio.charset.StandardCharsets.UTF_8.toString());
                    fileUrl = "/songs/file/" + enc;
                }

                jsonSongs.put(new JSONObject()
                    .put("id", song.getId())
                    .put("title", song.getTitle())
                    .put("artistId", song.getArtistId())
                    .put("filePath", fileUrl)
                    .put("coverArt", song.getCoverArt())
                    .put("genre", song.getGenre()));
            }

            response.setContentType("application/json");
            response.getWriter().write(jsonSongs.toString());

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            JSONObject err = new JSONObject();
            err.put("success", false);
            err.put("message", "Error: " + e.getMessage());
            response.getWriter().write(err.toString());
        }
    }
}