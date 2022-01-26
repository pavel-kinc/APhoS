let uploadedFilesCount;

//TODO refactor this garbage!!!
function uploadFiles() {
    uploadedFilesCount = 0;
    const uploadMessageArea = createScrollPanel();
    const inputElement = document.getElementById("fileSubmitter");
    const files = inputElement.files;
    document.getElementById("progressBar").style.visibility = "visible";
    document.getElementById("progressSpinner").style.visibility = "visible";
    document.getElementById("processingSign").style.visibility = "hidden";
    document.getElementById("processingCheck").style.visibility = "hidden";
    document.getElementById("processingMessage").style.visibility= "hidden";
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
            let i = 1;
            printUploadMessages(true, files[0].name,
                uploadProgress, filesLength, uploadMessageArea, pathToDir);
            do {
                let formData = new FormData();
                let file = files[i];
                let finalFetchBody = new FormData();
                finalFetchBody.append("pathToDir", pathToDir)
                if (uploadedFilesCount === filesLength) {
                    postFiles(myHeaders, finalFetchBody, filesLength);
                }
                formData.append("file", file);
                formData.append("dirName", pathToDir);
                fetch('/upload/save', {method: "POST", headers: myHeaders, body: formData})
                    .then(response => response.ok)
                    .then(success => {
                        printUploadMessages(success, file.name,
                            uploadProgress, filesLength, uploadMessageArea);
                        if (uploadedFilesCount === filesLength) {
                            postFiles(myHeaders, finalFetchBody, filesLength)
                        }
                    });
                i++;
            } while (i < filesLength);
        })
    document.getElementById("submitButton").disabled = true;
}

function postFiles(headers, body, filesLength) {
    fetch("/upload/parse", {
        method: "POST",
        headers: headers, body: body
    })
        .then(response => response.text())
        .then(body => finishedSaving(filesLength.toString(),body));
    document.getElementById("processingSign").style.visibility = "visible";
    document.getElementById("processingSpinner").style.visibility = "visible";
}

function finishedSaving(fileCount, errorCount) {
    let successCount = (fileCount.valueOf() - errorCount.valueOf()).toString()
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