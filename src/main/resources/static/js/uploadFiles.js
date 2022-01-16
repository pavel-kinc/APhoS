let uploadedFilesCount;

function uploadFiles() {
    uploadedFilesCount = 0;
    const uploadMessageArea = createScrollPanel();
    const inputElement = document.getElementById("fileSubmitter");
    const files = inputElement.files;
    document.getElementById("progressBar").style.visibility = "visible";
    document.getElementById("spinner").style.visibility = "visible";
    const uploadProgress = document.getElementById("uploadProgress");
    uploadProgress.style.width = "0";
    let filesLength = files.length;
    const myHeaders = new Headers();
    myHeaders.append('X-XSRF-TOKEN', Cookies.get('XSRF-TOKEN'));
    for (let i = 0; i < filesLength; i++) {
        let formData = new FormData();
        let file = files[i];
        formData.append("file", file);
        fetch('/upload/save', {method: "POST", headers: myHeaders, body: formData})
            .then(response => response.ok)
            .then(success => printMessages(success, file.name, uploadProgress, filesLength, uploadMessageArea));
    }
    document.getElementById("submitButton").disabled = true;
}

function printMessages(success, fileName, uploadProgress, filesLength, uploadMessageArea) {
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
        document.getElementById("spinner").style.visibility = "hidden";
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