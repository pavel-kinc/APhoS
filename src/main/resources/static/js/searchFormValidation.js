const searchForm = document.getElementById("searchForm");

searchForm.addEventListener('submit', function (event) {
    if (!searchForm.checkValidity()) {
        event.preventDefault();
        event.stopPropagation();
    }
    let minInput = document.getElementById("minInput");
    let maxInput = document.getElementById("maxInput");
    if (minInput.valueOf() > maxInput.valueOf()) {
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