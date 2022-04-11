let uploadedFilesCount;

async function uploadFiles() {
    uploadedFilesCount = 0;
    const uploadMessageArea = createScrollPanel();
    const inputElement = document.getElementById("fileSubmitter");
    const files = inputElement.files;
    let filesLength = files.length;
    displayUploadBeginningElements();
    const uploadProgress = document.getElementById("uploadProgress");
    uploadProgress.style.width = "0";
    const reqHeaders = new Headers();
    let formData = new FormData();
    let pathToDir = "create_new";
    let success = false;
    reqHeaders.append('X-XSRF-TOKEN', Cookies.get('XSRF-TOKEN'));
    for (let i = 0; i < filesLength; i++) {
        await new Promise(r => setTimeout(r, 100));
        formData.set("file", files[i]);
        formData.set("dir-name", pathToDir);
        // we want to create the tmp folder with the first file
        if (i === 0) {
            pathToDir = (await (await fetch('/upload/save',
                {method: "POST", headers: reqHeaders, body: formData})).text()).valueOf();
            success = true;
        } else {
            success = (await fetch('/upload/save',
                {method: "POST", headers: reqHeaders, body: formData})).ok;
        }
        printUploadMessages(success, files[i].name,
            uploadProgress, filesLength, uploadMessageArea);
    }
    parseFiles(reqHeaders, pathToDir, filesLength);
}

function parseFiles(headers, pathToDir, filesLength) {
    let parseRequestBody = new FormData();
    parseRequestBody.append("file-count", filesLength.toString());
    parseRequestBody.append("path-to-dir", pathToDir);
    fetch("/upload/parse", {
        method: "POST",
        headers: headers, body: parseRequestBody
    })
        .then(response => response.text())
        .then(body => finishedSaving(filesLength.toString(), body));
    document.getElementById("processingSign").style.visibility = "visible";
    document.getElementById("processingSpinner").style.visibility = "visible";
}

function finishedSaving(fileCount, errorCount) {
    let successCount;
    if (errorCount === "IOError") {
        successCount = 0;
    } else {
        successCount = (fileCount.valueOf() - errorCount.valueOf()).toString()
    }
    document.getElementById("processingSpinner").style.visibility = "hidden";
    document.getElementById("processingCheck").style.visibility = "visible";
    let messageFinished = document.getElementById("processingMessage");
    messageFinished.innerText = successCount + "/" + fileCount + " files saved successfully";
    messageFinished.style.visibility = "visible";
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
    if (progress === "100") {
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
    scrollPanel.id = "uploadMessages";
    mainColumn.appendChild(scrollPanel);
    return scrollPanel;
}


function displayUploadBeginningElements() {
    document.getElementById("progressBar").style.visibility = "visible";
    document.getElementById("progressSpinner").style.visibility = "visible";
    document.getElementById("processingSign").style.visibility = "hidden";
    document.getElementById("processingCheck").style.visibility = "hidden";
    document.getElementById("processingMessage").style.visibility = "hidden";
    document.getElementById("submitButton").disabled = true;
}