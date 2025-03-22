document.addEventListener("DOMContentLoaded", function () {
    const sidebar = document.querySelector(".sidebar");
    const toggleBtn = document.createElement("button"); // Nút mở/đóng sidebar
    toggleBtn.innerHTML = "☰";
    toggleBtn.classList.add("toggle-btn");

    document.body.appendChild(toggleBtn);

    // Sự kiện ẩn/hiện sidebar khi nhấn nút
    toggleBtn.addEventListener("click", function () {
        sidebar.classList.toggle("active");
        if (sidebar.classList.contains("active")) {
            toggleBtn.innerHTML = "✖";
        } else {
            toggleBtn.innerHTML = "☰";
        }
    });

    // Hiệu ứng khi click vào menu
    const menuItems = document.querySelectorAll(".sidebar ul li a");
    menuItems.forEach(item => {
        item.addEventListener("click", function () {
            menuItems.forEach(i => i.classList.remove("active"));
            this.classList.add("active");
        });
    });
});
