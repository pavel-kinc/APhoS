document.getElementById("textArea")
    .addEventListener("input", preventSpecialCharInput, false);

function saveChanges() {
    const description = document.getElementById("textArea");
    const userName = document.getElementById("usernameInput");
    const myHeaders = new Headers();
    myHeaders.append('X-XSRF-TOKEN', Cookies.get('XSRF-TOKEN'));
    let formData = new FormData();
    formData.append("description", description.value);
    formData.append("username", userName.value);
    fetch('/profile/save', {method: "POST", headers: myHeaders, body: formData, redirect:"follow"})
        .then(response => redirectAfterPost(response.url));
}

function redirectAfterPost(url) {
    const urlParameters = new URLSearchParams(url);
    if (urlParameters.get("editable")) {
        let usernameInput = document.getElementById("usernameInput")
        usernameInput.classList.add("is-invalid");
        document.getElementById("usernameTaken").style.visibility = "visible";
    } else {
        window.location.replace(url);
    }
}

function preventSpecialCharInput(event){
    event.target.value = event.target.value
        .replace(/[;/{}|\\"$<>\?~!`#\^&*@()%=\-\[\]\\']/g, "");
}