function calculate1RM() {
    var weight = document.getElementById('weight-text').value;
    var reps = document.getElementById('reps-text').value;
    document.getElementById('result').value=weight * (1 + (reps / 30));
}
function updateWeight(val) {
    document.getElementById('weight-text').value=val;
}
function updateReps(val) {
    document.getElementById('reps-text').value=val;
}