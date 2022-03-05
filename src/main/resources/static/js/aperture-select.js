function selectAperture() {
    let searchParams = window.location.search;
    let selects = document.getElementById("apertureSelects")
        .getElementsByTagName("select");
    let selectsArray = Array.from(selects);
    selectsArray.sort((a, b) => a.id - b.id);
    let aperturesParam = "";
    let selectValues = [];
    for (let select of selectsArray) {
        aperturesParam += "&apertures=" + select.value;
        selectValues.push(select.value);
    }
    window.location.replace(searchParams + aperturesParam);
    for (let i = 0; i < selectsArray.length; i++) {
        selectsArray[i].value=selectValues[i];
    }
}
