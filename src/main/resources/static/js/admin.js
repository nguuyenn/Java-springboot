document.addEventListener("DOMContentLoaded", function () {
    const sidebar = document.getElementById("sidebar");
    const toggleSidebarBtn = document.getElementById("toggleSidebar");
    const avatar = document.getElementById("avatar");
    const dropdown = document.getElementById("dropdown");

    // Toggle Sidebar
    toggleSidebarBtn.addEventListener("click", function () {
        sidebar.classList.toggle("collapsed");
    });

    // Toggle Dropdown
    avatar.addEventListener("click", function () {
        dropdown.classList.toggle("show");
    });

    // Đóng dropdown khi click ra ngoài
    document.addEventListener("click", function (event) {
        if (!avatar.contains(event.target) && !dropdown.contains(event.target)) {
            dropdown.classList.remove("show");
        }
    });
});
