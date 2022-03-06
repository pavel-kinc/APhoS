let apertures = new URLSearchParams(window.location.search).get("apertures");
let apsArray = apertures.split(",");
for (let i = 0; i < apsArray.length; i++) {
    document.getElementById(i.toString()).value=apsArray[i];
}



function selectAperture() {
    let searchParams = new URLSearchParams(window.location.search);
    searchParams.delete("apertures");
    let selects = document.getElementById("apertureSelects")
        .getElementsByTagName("select");
    let selectsArray = Array.from(selects);
    selectsArray.sort((a, b) => a.id - b.id);
    let aperturesParam = "";
    for (let i = 0; i < selectsArray.length; i++) {
        aperturesParam += selectsArray[i].value;
        if (i !== selectsArray.length - 1) {
            aperturesParam += ",";
        }
    }
    window.location.replace("object?" + searchParams.toString() + "&apertures="+ aperturesParam);
}
