/*
    Script for min magnitude slider to be be less than max magnitude slider.
 */


minRange = document.getElementById("minRange");
maxRange = document.getElementById("maxRange");
minInput = document.getElementById("minInput");
maxInput = document.getElementById("maxInput");
minRange.addEventListener("change", function (event) {

})

maxRange.addEventListener("change", function (event) {

})
minRange.oninput = function () {
    if (parseInt(minRange.value) > parseInt(maxRange.value)) {
        minRange.value = maxRange.value;
    }
    minInput.value = this.value;
}
maxRange.oninput = function () {
    if (parseInt(minRange.value) > parseInt(maxRange.value)) {
        maxRange.value = minRange.value;
    }
    maxInput.value = this.value;
}
maxInput.onchange = function () {
    maxRange.value = this.value;
}
minInput.onchange = function () {
    minRange.value = this.value;
}
