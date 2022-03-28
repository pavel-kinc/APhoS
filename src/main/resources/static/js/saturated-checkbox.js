let searchParams = new URLSearchParams(window.location.search);
if (searchParams.get("show-saturated")==="true") {
    document.getElementById("saturatedCheckbox").checked = true;
}

function saturatedSwitch() {
    let searchParams = new URLSearchParams(window.location.search);
    searchParams.delete("show-saturated");
    let saturatedCheckBox = document.getElementById("saturatedCheckbox");
    window.location.replace("object?" + searchParams.toString() + "&show-saturated=" + saturatedCheckBox.checked);
}