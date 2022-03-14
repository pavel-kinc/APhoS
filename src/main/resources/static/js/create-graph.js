fluxes.sort((flux1, flux2) => flux1.expMiddle.localeCompare(flux2.expMiddle));

const data = [];
for (let i = 0; i < fluxes.length; i++) {
    data[i] = {x: fluxes[i].expBegin, y: fluxes[i].magnitude};
}


const ctx = document.getElementById('lightCurveCanvas');
let chart = new Chart(ctx, {
    type: 'line',
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
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: 'Light curve of ' + name
            },
            zoom: {
                zoom: {
                    pan: {
                        enabled: true,
                    },
                    wheel: {
                        enabled: true,
                    },
                    pinch: {
                        enabled: true
                    },
                    mode: 'xy',
                }
            }
        }
    },
});

function updateChart() {
    let unwantedUsers = Array.from(unwantedSelect.options).map(option => option.id);
    let fluxesFiltered = fluxes.filter(flux => !unwantedUsers.includes(flux.username));
    fluxesFiltered.sort((flux1, flux2) => flux1.expMiddle.localeCompare(flux2.expMiddle));

    let data = [];
    for (let i = 0; i < fluxesFiltered.length; i++) {
        data[i] = {x: fluxesFiltered[i].expMiddle, y: fluxesFiltered[i].magnitude};
    }
    // data[data.length] = {x: "2021-11-12 16:31:25.355", y: 3};
    chart.data.datasets.forEach((dataset) => {
        dataset.data = data;
    });
    chart.update();
}