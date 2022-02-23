const searchForm = document.getElementById("searchForm");

searchForm.addEventListener('submit', function (event) {
    if (!searchForm.checkValidity()) {
        event.preventDefault();
        event.stopPropagation();
    }
    let minInput = document.getElementById("minInput");
    let maxInput = document.getElementById("maxInput");
    if (parseInt(minInput.value) > parseInt(maxInput.value)) {
        event.preventDefault();
        event.stopPropagation();
        minInput.classList.add("is-invalid");
        maxInput.classList.add("is-invalid");
    }
    let raInput = document.getElementById("raInput");
    let decInput = document.getElementById("decInput");
    let radiusInput = document.getElementById("radiusInput");
    // if one of the values is given we need all of them
    let emptyCoorInputs = [];
    if (raInput.value === "") {
        emptyCoorInputs.push(raInput);
    }
    if (decInput.value === "") {
        emptyCoorInputs.push(decInput);
    }
    if (radiusInput.value === "") {
        emptyCoorInputs.push(radiusInput);
    }
    if (emptyCoorInputs.length !== 0 && emptyCoorInputs.length !== 3) {
        event.preventDefault();
        event.stopPropagation();
        emptyCoorInputs.map(input => input.classList.add("is-invalid"))
    }
    let inputs = searchForm.getElementsByTagName("input");
    for (let input of inputs) {
        if (!input.checkValidity()) {
            input.classList.add("is-invalid");
        }
    }
})