function highlightActive() {
    let navMenu = document.getElementById('navMenu');
    let navLinks = navMenu.getElementsByClassName("nav-link");
    for (let navLink of navLinks) {
        if (navLink.href === document.URL) {
            navLink.classList.add('active');
        } else {
            navLink.classList.remove('active');
        }
    }
}

highlightActive();