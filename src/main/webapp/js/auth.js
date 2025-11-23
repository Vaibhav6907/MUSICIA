// Auth related functions
const auth = {
    login: async (username, password) => {
        try {
            const response = await fetch('/auth/login', {
                method: 'POST',
                credentials: 'same-origin',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`
            });

            const data = await response.json();
            if (data.success) {
                // Store user in localStorage for client-side use
                localStorage.setItem('user', JSON.stringify(data.user));
                console.log('Login successful:', data.user);
                window.location.href = '/index.html';
            } else {
                throw new Error(data.message || 'Login failed');
            }
        } catch (error) {
            console.error('Login error:', error);
            throw error;
        }
    },

    register: async (username, password, email, isArtist) => {
        try {
            const response = await fetch('/auth/register', {
                method: 'POST',
                credentials: 'same-origin',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}&email=${encodeURIComponent(email)}&isArtist=${isArtist}`
            });

            const data = await response.json();
            if (data.success) {
                alert('Registration successful! Please login.');
                window.location.href = '/login.html';
            } else {
                throw new Error(data.message || 'Registration failed');
            }
        } catch (error) {
            console.error('Registration error:', error);
            throw error;
        }
    },

    logout: async () => {
        try {
            await fetch('/auth/logout', { 
                method: 'POST', 
                credentials: 'same-origin' 
            });
            localStorage.removeItem('user');
            console.log('Logout successful');
            window.location.href = '/login.html';
        } catch (error) {
            console.error('Logout error:', error);
        }
    },

    checkAuth: () => {
        const user = localStorage.getItem('user');
        
        // Check if user is on a protected page
        const currentPath = window.location.pathname;
        const isLoginPage = currentPath.includes('login.html') || currentPath.includes('register.html');
        const isRootOrHome = currentPath === '/' || currentPath.endsWith('/index.html') || currentPath.endsWith('/upload.html');

        // If on protected page and no user, redirect to login
        if (isRootOrHome && !user) {
            console.log('No user found, redirecting to login');
            window.location.href = '/login.html';
            return null;
        }

        // If on login/register page and user is logged in, redirect to home
        if (isLoginPage && user) {
            window.location.href = '/index.html';
            return JSON.parse(user);
        }

        return user ? JSON.parse(user) : null;
    },

    // Verify session with server
    verifySession: async () => {
        try {
            const response = await fetch('/auth/verify', {
                method: 'GET',
                credentials: 'same-origin'
            });
            
            if (response.ok) {
                const data = await response.json();
                if (data.success && data.user) {
                    localStorage.setItem('user', JSON.stringify(data.user));
                    return data.user;
                }
            }
            return null;
        } catch (error) {
            console.error('Session verification error:', error);
            return null;
        }
    }
};

// Initialize auth check on page load
document.addEventListener('DOMContentLoaded', () => {
    auth.checkAuth();
});