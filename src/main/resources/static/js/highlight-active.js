/*
    Highlight active navbar element.
 */


function highlightActive() {
    let navMenu = document.getElementById('navMenu');
    let navLinks = navMenu.getElementsByClassName("nav-link");
    for (let navLink of navLinks) {
        if (navLink.href === document.URL ||
            navLink.href === document.URL.split("#")[0]) {
            navLink.classList.add('active');
        } else if (navLink.innerText === "Home" &&
            (document.URL.includes("search")) || document.URL.includes("reference")) {

        } else {
            navLink.classList.remove('active');
        }
    }
}

highlightActive();