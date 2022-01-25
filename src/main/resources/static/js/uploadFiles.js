let uploadedFilesCount;

function uploadFiles() {
    uploadedFilesCount = 0;
    const uploadMessageArea = createScrollPanel();
    const inputElement = document.getElementById("fileSubmitter");
    const files = inputElement.files;
    document.getElementById("progressBar").style.visibility = "visible";
    document.getElementById("progressSpinner").style.visibility = "visible";
    const uploadProgress = document.getElementById("uploadProgress");
    uploadProgress.style.width = "0";
    let filesLength = files.length;
    const myHeaders = new Headers();
    let formData = new FormData();
    formData.append("file", files[0]);
    formData.append("dirName", "create_new");
    myHeaders.append('X-XSRF-TOKEN', Cookies.get('XSRF-TOKEN'));
    fetch('/upload/save', {method: "POST", headers: myHeaders, body: formData})
        .then(response => response.text())
        .then(pathToDir => {
            printUploadMessages(true, files[0].name,
                uploadProgress, filesLength, uploadMessageArea, );
            for (let i = 1; i < filesLength; i++) {
            let formData = new FormData();
            let file = files[i];
            formData.append("file", file);
            formData.append("dirName", pathToDir);
            fetch('/upload/save', {method: "POST", headers: myHeaders, body: formData})
                .then(response => response.ok)
                .then(success => printUploadMessages(success, file.name,
                    uploadProgress, filesLength, uploadMessageArea));
            }
        })
    document.getElementById("submitButton").disabled = true;
}

function printUploadMessages(success, fileName, uploadProgress, filesLength, uploadMessageArea) {
    const paragraph = document.createElement("p");
    const message = document.createTextNode(
        success ?
             fileName + " uploaded successfully" :
             "There's been an error uploading " + fileName);
    paragraph.appendChild(message);
    let progress = Math.ceil((++uploadedFilesCount) / (filesLength / 100)).toString();
    uploadMessageArea.appendChild(paragraph);
    uploadProgress.ariaValueNow = progress;
    uploadProgress.style.width = progress + "%";
    if (progress==="100") {
        document.getElementById("progressSpinner").style.visibility = "hidden";
    }
}

function createScrollPanel() {
    let oldPanel = document.getElementById("uploadMessages");
    if (oldPanel != null) {
        oldPanel.remove();
    }
    const mainColumn = document.getElementById("mainColumn");
    const scrollPanel = document.createElement("div");
    scrollPanel.classList.add("scrollable", "mt-4");
    scrollPanel.id="uploadMessages";
    mainColumn.appendChild(scrollPanel);
    return scrollPanel;
}