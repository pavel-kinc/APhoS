function uploadFiles() {
    const inputElement = document.getElementById("fileSubmitter");
    const files = inputElement.files;
    document.getElementById("progressBar").style.visibility = "visible";
    const uploadProgress = document.getElementById("uploadProgress");
    uploadProgress.style.width = "0";
    let filesLength = files.length;
    for (let i = 0; i < filesLength; i++) {
        let formData = new FormData();
        let file = files[i];
        formData.append("file", file);
        fetch('/upload/save', {method: "POST", body: formData})
            .then(response => response.ok)
            .then(success => console.log(success ? "OK" : "NOK"));
        let progress = Math.ceil((i+1) / (filesLength / 100)).toString();
        uploadProgress.ariaValueNow = progress;
        uploadProgress.style.width = progress + "%";
    }
    document.getElementById("submitButton").disabled = true;
}
