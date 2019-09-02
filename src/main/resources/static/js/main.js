let OkToToggleLogo = true; // Global flag for logo visibility toggling

$(document).ready(function () {

    /* This event listener is responsible for hiding the logo container and
     making the tab navigation bar stick to the top of the screen when user is
     scrolling down the page and showing the logo container and unsticking the
     navigation bar when the page is scrolled back to the very top. It has to be
     coordinated with switching tabs, which automatically scrolls page to the
     very top.*/

    let logo = document.getElementById('logo-container');
    let navbar = document.getElementById('big-tabs');

    // Fillers are to prevent bootstrap graphical glitches
    let filler1 = document.getElementById('filler-1');
    let filler2 = document.getElementById('filler-2');

    window.addEventListener('scroll', function () {
        let pagePosition = $('html').scrollTop();

        // Is it OK to toggle logo visibility?
        if (OkToToggleLogo) {

            // Is the page scrolled down and is logo visible? Hide logo.
            if (pagePosition !== 0 && pagePosition !== 1 && logo.style.display !== 'none') {

                navbar.classList.add('sticky');
                filler1.classList.add('sticky');
                $(logo).slideUp('fast').queue(false);
                $(filler2).show('fast').queue(false);
                $(filler1).show('fast').queue(false);

                // Is the page scrolled up and is logo hidden? Show logo.
            } else if (pagePosition === 0 && logo.style.display === 'none') {

                navbar.classList.remove('sticky');
                filler1.classList.remove('sticky');
                $(filler1).slideUp('fast').queue(false);
                $(filler2).slideUp('fast').queue(false);
                $(logo).show('fast').queue(false);
            }

            /* If logo toggling is not allowed, but the page is scrolled
             all the way up, allow logo toggling next time. */
        } else if (pagePosition === 0) {
            OkToToggleLogo = true;
        }
    });

    /* These handlers are responsible for scrolling the document to the top
    when user is changing tabs, while not allowing the logo toggle action to be
    triggered by the scrolling (only if the content is larger than screen. */
    $('#tab1handle, #tab2handle, #tab3handle, #tab4handle')
        .bind('click', function () {
            scrollToTop()
        });
});

/* Auxiliary function for tab change event handlers */
function scrollToTop() {
    OkToToggleLogo = false;
    $('html').animate({scrollTop: 1}, 'fast');
    OkToToggleLogo = true;
}

/* This function updates a single lift section in General Strength tab
   whenever the user moves the slider it updates the corresponding number
   field and calculates 1 rep max when number field is updated it moves the
   slider and 1 rep max is calculated.
   Parameters are:
    fieldToUpdate - id of the corresponding field to update
    value - value that fieldToUpdate will be updated with
    weightField - id of the field holding weight for the current lift
    repsField - id of the field holding repetitions for the current lift
    maxField - id of the field holding the 1 rep max for the current lift
               that will be calculated */
function updateGS(fieldToUpdate, value, weightField, repsField, maxField) {
    document.getElementById(fieldToUpdate).value = value;
    let weight = document.getElementById(weightField).value;
    let reps = document.getElementById(repsField).value;
    document.getElementById(maxField).value = Math.round(weight * (1 + (reps / 30)) * 100) / 100;
}

/* This function updates an element of certain id with given value */
function update(field, val) {
    document.getElementById(field).value = val;
}

/* This function calculates the 1 rep max and percentages in 1 Rep Max tab */
function calculate1RM() {
    let weight = document.getElementById('weight-text').value;
    let reps = document.getElementById('reps-text').value;
    let result = weight * (1 + (reps / 30));
    document.getElementById('result').value
        = result.toFixed(2);

    for (let i = 1; i < 14; i++) {
        document.getElementById('percentage-' + i).value =
            (document.getElementById('percentage-input-' + i)
                .value * 0.01 * result).toFixed(2);
    }
}

/* This function updates descriptions for percentages of 1 Rep Max */
function updatePercentageDescription(description, percentage) {
    let x = 0;
    let descriptions = [
        "Upper body ballistic work, lower body plyometrics. Improves muscle " +
        "hardness and contraction speed.",
        "Lower body ballistic work. Improves muscle hardness, develops power " +
        "and contraction speed.",
        "Explosiveness, speed and power. Great for muscle hardness. 60% can " +
        "be used for hypertrophy when done to failure.",
        "Best range for muscle mass building, also good for explosiveness" +
        " (~70%) and strength (~80%) in Olympic lifting.",
        "Best for building muscle strength, use ~80% for easy recovery, ~90%" +
        " for quick strength peaking.",
        "Increases maximal strength via neural factors. Good for displaying" +
        " strength. Difficult to recover from."];
    if (percentage < 21) {
        x = 0;
    } else if (percentage < 41) {
        x = 1;
    } else if (percentage < 61) {
        x = 2;
    } else if (percentage < 81) {
        x = 3;
    } else if (percentage < 91) {
        x = 4;
    } else {
        x = 5;
    }
    document.getElementById(description).innerHTML = descriptions[x];
}

/* This function displays the list of all user's workouts */
function getWorkoutsList() {
    let workoutsList = document.getElementById("workouts-list");
    let text = document.createTextNode("Here goes the workout list!");
    workoutsList.appendChild(text);
}

let workout = {workoutNotes:[], exercises:[]};
let shortcut = "";

function buildWorkout() {
    workout = {workoutNotes:[], exercises:[]};
    console.log(workout);
    document.getElementById("created").value = new Date();
}

function addExercise() {
    let exercise = {title:null, exerciseNotes:[], sets:[]};
    workout.exercises.push(exercise);
    console.log(workout);
    let exerciseNumber = workout.exercises.length;
    let newExercise = document.createElement('div');
    newExercise.innerHTML = "<br/><div id='exercise" + exerciseNumber + "-container'>" +
        "<label for='exercise" + exerciseNumber + "'>Exercise #" + exerciseNumber + ":</label>" +
        "<input type='text' name='exercise" + exerciseNumber + "' " +
        "id='exercise1" + exerciseNumber + "' minlength='20' value='' " +
        "onchange='addExerciseTitle(" + exerciseNumber + ", this.value)'/>" +
        "</div><div id='exercise" + exerciseNumber + "-notes'></div><br/>" +
        "<button class='my-button' onclick='addNote();'>Add exercise note</button><br/><br/>" +
        "<div id='exercise" + exerciseNumber + "-sets'></div><button class='my-button' " +
        "onclick='addSet(" + exerciseNumber + ");'>Add set</button><br/><br/>";
    document.getElementById("workout-exercises").appendChild(newExercise);
}

function addExerciseTitle(exerciseNumber, exerciseTitle) {
    workout.exercises[exerciseNumber-1].title = exerciseTitle;
    console.log(workout.exercises[exerciseNumber-1]);
}

function addSet(exerciseNumber) {
    let newSet = {setData:null, setNotes:[]};
    workout.exercises[exerciseNumber-1].sets.push(newSet);
    console.log(workout.exercises[exerciseNumber-1].sets);
}

function addNote() {
    let note = {noteType:0, noteContent:null};
    workout.workoutNotes.push(note);
    shortcut = 'workout-note' + workout.workoutNotes.length;
    console.log(workout.workoutNotes);
    let newNote = document.createElement('div');
    newNote.setAttribute('id', shortcut + '-container');
    newNote.innerHTML = "<label for='" + shortcut + "'>" +
        "Workout Note #" + workout[0] + ":</label><input type='text' " +
        "name='" + shortcut + "' id='" + shortcut + "' minlength='20' value=''/>" +
        "<select onchange='noteTypeSelectionDetection(this, shortcut);' " +
        "name='" + shortcut + "-type'><option value='0'>Text note</option>" +
        "<option value='1'>Audio note</option><option value='2'>Picture</option>" +
        "<option value='3'>Video</option></select></>";
    document.getElementById("workout-notes").appendChild(newNote);
}

function noteTypeSelectionDetection(selectFieldValue, fieldToReplaceID) {
    let replacement = document.createElement('input');
    replacement.setAttribute('type', 'file');
    switch (selectFieldValue.value) {
        case '0':
            break;
        case '1':

            replacement.setAttribute
            ('accept', 'audio/mpeg, audio/ogg, audio/wav');
            break;
        case '2':
            replacement.setAttribute
            ('accept', 'image/gif, image/jpeg, image/png');
            break;
        case '3':
            replacement.setAttribute
            ('accept', 'video/mp4, video/ogg, video/webm');
            break;
    }
    document.getElementById(fieldToReplaceID).setAttribute('id', 'tempID');
    let fieldToReplaceNode = document.getElementById('tempID');
    let fieldToReplaceParent = fieldToReplaceNode.parentElement;
    replacement.setAttribute('id', fieldToReplaceID);
    fieldToReplaceParent.replaceChild(replacement, fieldToReplaceNode);
}