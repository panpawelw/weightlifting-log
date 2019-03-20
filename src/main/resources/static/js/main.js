function calculate1RM() {
    var weight = document.getElementById('weight-text').value;
    var reps = document.getElementById('reps-text').value;
    var result = weight * (1 + (reps / 30));
    document.getElementById('result').value = result;
    document.getElementById('percentage-1').innerText =
        (document.getElementById('percentage-input-1').value  * 0.01 * result).toFixed(1);
    document.getElementById('percentage-2').innerText =
        (document.getElementById('percentage-input-2').value  * 0.01 * result).toFixed(1);
    document.getElementById('percentage-3').innerText =
        (document.getElementById('percentage-input-3').value  * 0.01 * result).toFixed(1);
    document.getElementById('percentage-4').innerText =
        (document.getElementById('percentage-input-4').value  * 0.01 * result).toFixed(1);
    document.getElementById('percentage-5').innerText =
        (document.getElementById('percentage-input-5').value  * 0.01 * result).toFixed(1);
    document.getElementById('percentage-6').innerText =
        (document.getElementById('percentage-input-6').value  * 0.01 * result).toFixed(1);
    document.getElementById('percentage-7').innerText =
        (document.getElementById('percentage-input-7').value  * 0.01 * result).toFixed(1);
    document.getElementById('percentage-8').innerText =
        (document.getElementById('percentage-input-8').value  * 0.01 * result).toFixed(1);
    document.getElementById('percentage-9').innerText =
        (document.getElementById('percentage-input-9').value  * 0.01 * result).toFixed(1);
    document.getElementById('percentage-10').innerText =
        (document.getElementById('percentage-input-10').value  * 0.01 * result).toFixed(1);
    document.getElementById('percentage-11').innerText =
        (document.getElementById('percentage-input-11').value  * 0.01 * result).toFixed(1);
    document.getElementById('percentage-12').innerText =
        (document.getElementById('percentage-input-12').value  * 0.01 * result).toFixed(1);
    document.getElementById('percentage-13').innerText =
        (document.getElementById('percentage-input-13').value  * 0.01 * result).toFixed(1);
}

function updateWeight(val) {
    document.getElementById('weight-text').value = val;
}

function updateReps(val) {
    document.getElementById('reps-text').value = val;
}