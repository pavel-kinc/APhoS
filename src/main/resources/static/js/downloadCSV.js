
function downloadCSV() {
    const searchParams = new URLSearchParams(window.location.search);
    const objectId = searchParams.get("id");
    let unwantedUsers = Array.from(unwantedSelect.options)
        .map(option => option.id.toString());
    const myHeaders = new Headers();
    let formData = new FormData();
    formData.append("objectID", objectId);
    formData.append("unwantedUsers", unwantedUsers);
    myHeaders.append('X-XSRF-TOKEN', Cookies.get('XSRF-TOKEN'));
    fetch('/object/download', {method: "POST", headers: myHeaders, body: formData})
        .then(response => response.blob())
        .then(blob => download(blob, "magnitudes"));
}
