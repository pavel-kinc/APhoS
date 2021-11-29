function signedInContent() {
    fetch("/user")
        .then(function (response) {
            displayLogInOut(response.ok)
            return response.json()
        })
        .then(body => displayName(body.name))
}

function displayName(userName) {
    let userDropDown = document.getElementById("userDropDown");
    userDropDown.innerText = userName == null ? "User" : userName;
}

function displayLogInOut(signedIn) {
    let signInOutButton = document.getElementById("signInOutButton");
    signInOutButton.innerText = signedIn ? "Sign-out" : "Sign-in";
}

signedInContent();