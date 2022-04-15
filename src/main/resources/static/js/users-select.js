/*
    Drag and drop filter for users when displaying fluxes.
 */


let unwantedSelect = document.getElementById("unwantedUsersSelect");
let wantedSelect = document.getElementById("wantedUsersSelect");
unwantedSelect.addEventListener("drop", drop);
unwantedSelect.addEventListener("dragover", allowDrop);
wantedSelect.addEventListener("dragover", allowDrop);
wantedSelect.addEventListener("drop", drop);
let users = document.getElementById("userDropDownMenu").getElementsByTagName("option");

for (let user of users) {
    user.addEventListener("dragstart", drag);
}


function drag(event) {
    event.dataTransfer.setData("text/plain", event.target.id);
    event.dataTransfer.effectAllowed = "move";
}

function allowDrop(event) {
    event.preventDefault();
    event.dataTransfer.dropEffect = "move";
}

function drop(event) {
    allowDrop(event);
    let droppedElement = event.dataTransfer.getData("text/plain");
    event.target.appendChild(document.getElementById(droppedElement));
}

function filterUsers() {
    let unwantedUsers = Array.from(unwantedSelect.options).map(option => option.id);
    let fluxTable = document.getElementById("fluxTable");
    let rows = fluxTable.getElementsByTagName("tr");
    let theadItems = fluxTable.tHead.rows[0].children;
    let usernameIndex = 9;
    for (let i = 0; i < theadItems.length; i++) {
        if (theadItems[i].innerText.trim().includes("Uploaded by")) {
            usernameIndex = i;
        }
    }
    Array.from(rows)
        .map(function (row) {
            if (unwantedUsers.includes(row.children.item(usernameIndex).innerText.trim())) {
                row.style.display = "none";
            } else {
                row.style.display = "";
            }
        });
}