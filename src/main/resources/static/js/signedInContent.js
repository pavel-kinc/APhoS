function signedInContent() {
    fetch("/user")
        .then(function (response) {
            displayLogInOut(response.ok)
            return response.json()
        })
        //.then(body => displayName(body.name))
        .catch(err => console.log(err));
}

function displayName(userName) {
    let userDropDown = document.getElementById("userDropDown");
    if (userDropDown.innerText !== userName) {
        userDropDown.innerText = userName;
    }
}

function displayLogInOut(signedIn) {
    let signInOutButton = document.getElementById("signInOutButton");
    if (!signedIn) {
        signInOutButton.innerText = "Sign-in";
        signInOutButton.href = "/oauth2/authorization/google";
    } else {
        signInOutButton.innerText = "Sign-out";
        signInOutButton.href = "/logout";
    }
}

signedInContent();