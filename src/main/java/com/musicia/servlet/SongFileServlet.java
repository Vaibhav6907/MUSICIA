package com.musicia.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class SongFileServlet extends HttpServlet {
    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB
    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // /{filename}
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String filename = java.net.URLDecoder.decode(pathInfo.substring(1), "UTF-8");
        String realPath = getServletContext().getRealPath(UPLOAD_DIR) + File.separator + filename;
        File file = new File(realPath);
        if (!file.exists() || !file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = getServletContext().getMimeType(file.getName());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        response.setHeader("Accept-Ranges", "bytes");
        response.setContentType(contentType);
        long length = file.length();
        long start = 0, end = length - 1;

        String range = request.getHeader("Range");
        if (range != null && range.startsWith("bytes=")) {
            String[] parts = range.substring(6).split("-");
            try {
                if (parts.length > 0 && parts[0].length() > 0) {
                    start = Long.parseLong(parts[0]);
                }
            } catch (NumberFormatException ignored) { }
            try {
                if (parts.length > 1 && parts[1].length() > 0) {
                    end = Long.parseLong(parts[1]);
                }
            } catch (NumberFormatException ignored) { }
            if (end > length - 1) end = length - 1;
            if (start > end) {
                response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return;
            }
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Content-Range", String.format("bytes %d-%d/%d", start, end, length));
        }

        long contentLength = end - start + 1;
        response.setHeader("Content-Length", String.valueOf(contentLength));

        try (RandomAccessFile raf = new RandomAccessFile(file, "r"); OutputStream out = response.getOutputStream()) {
            raf.seek(start);
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            long toRead = contentLength;
            int read;
            while (toRead > 0 && (read = raf.read(buffer, 0, (int)Math.min(buffer.length, toRead))) != -1) {
                out.write(buffer, 0, read);
                toRead -= read;
            }
        }
    }
}
