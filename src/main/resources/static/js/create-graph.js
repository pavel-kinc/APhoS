
fluxes.sort((flux1, flux2) => flux1.expBegin.localeCompare(flux2.expBegin))

const data = [];
for (let i = 0; i < fluxes.length; i++) {
    data[i] = {x: fluxes[i].expBegin, y: fluxes[i].magnitude};
}

const ctx = document.getElementById('lightCurveCanvas');
new Chart(ctx, {
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
                }
            }
        },
        plugins: {
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: 'Light curve of ' + name
            }
        }
    },
});