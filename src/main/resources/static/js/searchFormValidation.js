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
    let inputs = searchForm.getElementsByTagName("input");
    for (let input of inputs) {
        if (!input.checkValidity()) {
            input.classList.add("is-invalid");
        }
    }
})