
const ctx = document.getElementById('lightCurveCanvas');
new Chart(ctx, {
    type: 'line',
    data: {
        datasets: [{
            label: "Magnitude",
            data: [{x:'2016-12-25', y:20}, {x:'2016-12-26', y:10}]
        }]
    },
    options: {
        responsive: false,
        plugins: {
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: 'Light curve'
            }
        }
    },
});
function displayGraph() {
}