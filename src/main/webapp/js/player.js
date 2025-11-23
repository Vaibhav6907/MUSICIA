// Music player related functions
const player = {
    audio: null,
    currentSong: null,
    playlist: [],

    init: () => {
        player.audio = document.querySelector('#audio-player');
        player.loadSongs();
    },

    loadSongs: async () => {
        try {
            const response = await fetch('/songs', { credentials: 'same-origin' });
            if (!response.ok) {
                console.error('Failed to load songs:', response.status);
                return;
            }
            const songs = await response.json();
            player.playlist = songs;
            player.renderSongs(songs);
        } catch (error) {
            console.error('Error loading songs:', error);
        }
    },

    renderSongs: (songs) => {
        const grid = document.querySelector('.song-grid');
        if (!grid) return;

        const raw = localStorage.getItem('user');
        const currentUser = raw ? JSON.parse(raw) : null;

        grid.innerHTML = songs.map(song => {
            const isOwner = currentUser && currentUser.id === song.artistId;
            return `
            <div class="song-card">
                <div class="song-click" onclick="player.playSong(${song.id})">
                    <img src="${song.coverArt}" alt="${song.title}" class="song-cover">
                    <div class="song-info">
                        <div class="song-title">${song.title}</div>
                        <div class="song-artist">Artist ID: ${song.artistId}</div>
                    </div>
                </div>
                ${isOwner ? `<div class="song-actions">
                    <button class="btn btn-small btn-edit" onclick="player.editSong(event, ${song.id})" aria-label="Edit ${song.title}">
                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 20h9"/><path d="M16.5 3.5a2.1 2.1 0 0 1 3 3L7 19l-4 1 1-4 12.5-12.5z"/></svg>
                        Edit
                    </button>
                    <button class="btn btn-small btn-delete" onclick="player.deleteSong(event, ${song.id})" aria-label="Delete ${song.title}">
                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6m5 0V4a2 2 0 0 1 2-2h0a2 2 0 0 1 2 2v2"/></svg>
                        Delete
                    </button>
                </div>` : ''}
            </div>
        `}).join('');
    },

    editSong: async (e, songId) => {
        e.stopPropagation();
        const title = prompt('New title:');
        if (title === null) return;
        const genre = prompt('New genre: (optional)');
        try {
            const body = `title=${encodeURIComponent(title)}&genre=${encodeURIComponent(genre || '')}`;
            const res = await fetch(`/songs/${songId}`, { 
                method: 'PUT', 
                credentials: 'same-origin', 
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' }, 
                body 
            });
            const data = await res.json();
            if (data.success) {
                alert('Updated');
                player.loadSongs();
            } else {
                alert('Update failed: ' + (data.message || ''));
            }
        } catch (err) {
            console.error(err);
            alert('Update error');
        }
    },

    deleteSong: async (e, songId) => {
        e.stopPropagation();
        if (!confirm('Delete this song?')) return;
        try {
            const res = await fetch(`/songs/${songId}`, { 
                method: 'DELETE', 
                credentials: 'same-origin' 
            });
            const data = await res.json();
            if (data.success) {
                alert('Deleted');
                player.loadSongs();
            } else {
                alert('Delete failed: ' + (data.message || ''));
            }
        } catch (err) {
            console.error(err);
            alert('Delete error');
        }
    },

    playSong: (songId) => {
        const song = player.playlist.find(s => s.id === songId);
        if (!song) {
            console.error('Song not found:', songId);
            return;
        }

        player.currentSong = song;
        console.log('Playing:', song.title, 'URL:', song.filePath);
        player.audio.src = song.filePath;
        player.audio.play().catch(err => {
            console.error('Playback error:', err);
            alert('Failed to play audio');
        });
        player.updateNowPlaying();
    },

    updateNowPlaying: () => {
        const nowPlaying = document.querySelector('.now-playing');
        if (nowPlaying && player.currentSong) {
            nowPlaying.textContent = `Now Playing: ${player.currentSong.title}`;
        }
    }
};

// Upload related functions
const upload = {
    uploadSong: async (formData) => {
        try {
            console.log('Starting upload...');
            
            // Get the full URL to ensure proper routing
            const baseUrl = window.location.origin;
            const uploadUrl = baseUrl + '/songs';
            console.log('Upload URL:', uploadUrl);

            const response = await fetch(uploadUrl, {
                method: 'POST',
                credentials: 'include',
                body: formData
            });

            console.log('Upload response status:', response.status);
            
            if (!response.ok) {
                const errorText = await response.text();
                console.error('Upload error response:', errorText);
                throw new Error(`HTTP ${response.status}: ${errorText}`);
            }

            const data = await response.json();
            console.log('Upload response data:', data);

            if (data.success) {
                alert('Upload successful!');
                window.location.href = '/index.html';
            } else {
                throw new Error(data.message || 'Upload failed without message');
            }
        } catch (error) {
            console.error('Upload error:', error);
            alert('Upload failed: ' + error.message);
            throw error;
        }
    }
};

// Initialize player on page load
document.addEventListener('DOMContentLoaded', () => {
    player.init();
});