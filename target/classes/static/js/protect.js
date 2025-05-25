const userProtect = JSON.parse(localStorage.getItem('user'));
if (!userProtect) {
    window.location.href = 'index.html';
}
