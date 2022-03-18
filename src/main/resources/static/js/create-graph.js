fluxes.sort((flux1, flux2) => flux1.expMiddle.localeCompare(flux2.expMiddle));

const data = [];
const annotations = [];
for (let i = 0; i < fluxes.length; i++) {
    data[i] = {x: fluxes[i].expMiddle, y: fluxes[i].magnitude};
    annotations[i] = {
        type: 'line',
        yMin: fluxes[i].errorBottom,
        yMax: fluxes[i].errorTop,
        xMin: fluxes[i].expMiddle,
        xMax: fluxes[i].expMiddle,
        borderColor: 'rgb(149,188,232)',
        borderWidth: 1.8,
        display: true,
    }
}


const ctx = document.getElementById('lightCurveCanvas');
let chart = new Chart(ctx, {
    type: 'scatter',
    data: {
        datasets: [{
            data: data,
            backgroundColor: "#3c66ba",
            borderColor: "#6693cc",
            label: "magnitude",
        }]
    },
    options: {
        responsive: true,
        scales: {
            x: {
                type: 'time',
                time: {
                    unit: "hour",
                    displayFormats: {
                        hour: 'yyyy MMM dd HH:mm:ss'
                    }
                },
                title: {
                    display: false,
                    text: 'Time'
                }
            },
            y: {
                title: {
                    display: true,
                    text: 'Magnitude'
                },
                reverse: true
            }
        },
        plugins: {
            annotation: {
                annotations: annotations,
            },
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: 'Light curve of ' + name
            },
            zoom: {
                zoom: {
                    wheel: {
                        enabled: true,
                    },
                    pinch: {
                        enabled: true
                    },
                    mode: 'xy'
                },
                pan: {
                    enabled: true,
                },
            }
        },
    },
});

function updateErrorPlotting() {
    let checkbox = document.getElementById("errorCheckbox");
    if (!checkbox.checked) {
        chart.options.plugins.annotation.annotations = [];
    } else {
        chart.options.plugins.annotation.annotations = annotations;
    }
    chart.update();
}

function updateChart() {
    let unwantedUsers = Array.from(unwantedSelect.options).map(option => option.id);
    let fluxesFiltered = fluxes.filter(flux => !unwantedUsers.includes(flux.username));
    fluxesFiltered.sort((flux1, flux2) => flux1.expMiddle.localeCompare(flux2.expMiddle));
    let data = [];
    for (let i = 0; i < fluxesFiltered.length; i++) {
        data[i] = {x: fluxesFiltered[i].expMiddle, y: fluxesFiltered[i].magnitude};
    }
    chart.data.datasets.forEach((dataset) => {
        dataset.data = data;
    });
    chart.update();
}

function resetZoom() {
    chart.resetZoom();
}