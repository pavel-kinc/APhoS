minRange = document.getElementById("minRange");
maxRange = document.getElementById("maxRange");
minInput = document.getElementById("minInput");
maxInput = document.getElementById("maxInput");
minRange.oninput = function () {
    minInput.value = this.value;
}
maxRange.oninput = function () {
    maxInput.value = this.value;
}
maxInput.oninput = function () {
    maxRange.value = this.value;
}
minInput.oninput = function () {
    minRange.value = this.value;
}
minRange