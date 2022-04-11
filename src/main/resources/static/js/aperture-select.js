/*
    Script to change the chosen apertures in the url params on change in the selects.
*/

let apertures = new URLSearchParams(window.location.search).get("apertures");
if (apertures) {
    let apsArray = apertures.split(",");
    for (let i = 0; i < apsArray.length; i++) {
        document.getElementById(i.toString()).value = apsArray[i];
    }
}

let refApertures = new URLSearchParams(window.location.search).get("ref-apertures");
if (refApertures) {
    let apsRefArray = refApertures.split(",");
    for (let i = 0; i < apsRefArray.length; i++) {
        document.getElementById("ref" + i.toString()).value = apsRefArray[i];
    }
}

function selectAperture() {
    let searchParams = new URLSearchParams(window.location.search);
    searchParams.delete("apertures");
    searchParams.delete("ref-apertures");
    let selects = document.getElementById("apertureSelects")
        .getElementsByTagName("select");
    let selectsArray = Array.from(selects).filter(select => !select.id.includes("ref"));
    let selectsRefArray = Array.from(selects).filter(select => select.id.includes("ref"));
    selectsArray.sort((a, b) => a.id - b.id);
    selectsRefArray.sort((a, b) => a.id - b.id);
    let aperturesParam = "";
    let aperturesRefParam = "";
    for (let i = 0; i < selectsArray.length; i++) {
        aperturesParam += selectsArray[i].value;
        if (i !== selectsArray.length - 1) {
            aperturesParam += ",";
        }
        aperturesRefParam += selectsRefArray[i].value;
        if (i !== selectsRefArray.length - 1) {
            aperturesRefParam += ",";
        }
    }
    window.location.replace("object?" + searchParams.toString()
        + "&apertures=" + aperturesParam + "&ref-apertures=" + aperturesRefParam);
}
