/*
    Script supplying the whole file upload functionality:
    Posting the requests as well as changing the GUI.
 */


let progressBarCount;
const progressBar = document.getElementById("uploadProgress");

async function uploadFiles() {
    progressBarCount = 0;
    const uploadMessageArea = createScrollPanel();
    const inputElement = document.getElementById("fileSubmitter");
    const files = inputElement.files;
    let filesLength = files.length;
    updateProgress(filesLength);
    displayUploadBeginningElements();
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
        printUploadMessages(success, files[i].name, filesLength, uploadMessageArea);
    }
    parseFiles(reqHeaders, pathToDir, filesLength);
}

function parseFiles(headers, pathToDir, filesLength) {
    progressBarCount = 0;
    updateProgress(filesLength);
    let emitter = new EventSource(
        '/upload/parse?path-to-dir=' + pathToDir + '&file-count=' + filesLength);
    emitter.addEventListener("FILE_STORED", function () {
        ++progressBarCount;
        updateProgress(filesLength);
    });
    emitter.addEventListener("COMPLETED", function (e) {
        let numOfUnsuccessfull = e.data;
        emitter.close();
        finishedSaving(filesLength, numOfUnsuccessfull);
    });
    emitter.onerror = function (e) {
        document.getElementById("error").style.display = "inline";
    };
    document.getElementById("uploading").style.display = "none";
    document.getElementById("processingRow").style.display = "inline";
    document.getElementById("processingSign").style.visibility = "visible";
    document.getElementById("processingSpinner").style.visibility = "visible";
}

function finishedSaving(fileCount, errorCount) {
    let successCount;
    if (errorCount === "IOError") {
        successCount = 0;
    } else {
        successCount = (fileCount.valueOf() - errorCount.valueOf()).toString();
    }
    document.getElementById("processingSpinner").style.visibility = "hidden";
    document.getElementById("processingCheck").style.visibility = "visible";
    let messageFinished = document.getElementById("processingMessage");
    messageFinished.innerText = successCount + "/" + fileCount + " files saved successfully";
    messageFinished.style.visibility = "visible";
}

function printUploadMessages(success, fileName, filesLength, uploadMessageArea) {
    const paragraph = document.createElement("p");
    const message = document.createTextNode(
        success ?
            fileName + " uploaded successfully" :
            "There's been an error uploading " + fileName);
    progressBarCount++;
    updateProgress(filesLength);
    paragraph.appendChild(message);
    uploadMessageArea.appendChild(paragraph);
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


function updateProgress(filesCount) {
    let progress = Math.ceil((progressBarCount) / (filesCount / 100)).toString();
    progressBar.ariaValueNow = progress;
    progressBar.style.width = progress + "%";
}

function displayUploadBeginningElements() {
    progressBar.style.visibility = "visible";
    document.getElementById("processingCheck").style.visibility = "hidden";
    document.getElementById("processingSign").style.visibility = "hidden";
    document.getElementById("processingRow").style.display = "none";
    document.getElementById("uploading").style.display = "inline";
    document.getElementById("submitButton").disabled = true;
}