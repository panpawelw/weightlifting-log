let OkToToggleLogo = true; // Global flag for logo visibility toggling

$(document).ready(function () {

    /* This event listener is responsible for hiding the logo container and
     making the tab navigation bar stick to the top of the screen when user is
     scrolling down the page and showing the logo container and unsticking the
     navigation bar when the page is scrolled back to the very top. It has to be
     coordinated with switching tabs, which automatically scrolls the page to
     the very top.*/

    const logo = document.getElementById('logo-container');
    const navbar = document.getElementById('big-tabs');

    // Fillers are to prevent bootstrap graphical glitches
    const filler1 = document.getElementById('filler-1');
    const filler2 = document.getElementById('filler-2');

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
    triggered by the scrolling (only if the content is larger than screen). */
    $('#tab1handle, #tab2handle, #tab3handle, #tab4handle').bind('click', function () {
        scrollToTop();
    });

    window.addEventListener('input', function(event) {
        let currentItem = event.target;
        if (currentItem.className === 'my-input') {
            let data = $(currentItem).data('set');
            switch(data.length){
                case 1:
                    workout[data[0]] = currentItem.innerHTML;
                    break;
                case 3:
                    workout[data[0]][data[1]][data[2]] = currentItem.innerHTML;
                    break;
                case 5:
                    workout[data[0]][data[1]][data[2]][data[3]][data[4]] = currentItem.innerHTML;
                    break;
                case 7:
                    workout[data[0]][data[1]][data[2]][data[3]][data[4]][data[5]][data[6]]
                        = currentItem.innerHTML;
                    break;
            }
        }
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
    let descriptionText;
    if (percentage < 21) {
        descriptionText = "Upper body ballistic work, lower body plyometrics. Improves muscle " +
            "hardness and contraction speed.";
    } else if (percentage < 41) {
        descriptionText = "Lower body ballistic work. Improves muscle hardness, develops power " +
            "and contraction speed.";
    } else if (percentage < 61) {
        descriptionText = "Explosiveness, speed and power. Great for muscle hardness. 60% can " +
            "be used for hypertrophy when done to failure.";
    } else if (percentage < 81) {
        descriptionText = "Best range for muscle mass building, also good for explosiveness" +
            " (~70%) and strength (~80%) in Olympic lifting.";
    } else if (percentage < 91) {
        descriptionText = "Best for building muscle strength, use ~80% for easy recovery, ~90%" +
            " for quick strength peaking.";
    } else {
        descriptionText = "Increases maximal strength via neural factors. Good for displaying" +
            " strength. Difficult to recover from.";
    }
    document.getElementById(description).innerHTML = descriptionText;
}

/* Workout-related functions */
let workout = null;
let files = [];

function buildWorkout() {
    workout = {
        id: 0, title: null, created: null, updated: null, user: null,
        notes: [], exercises: []
    };
    const created = new Date().toISOString().slice(0, 19).replace('T', ' ');
    document.getElementById("created").value = created;
    workout['created'] = created;
    document.getElementById('title').setAttribute('data-set', '["title"]');
}

function addExercise() {
    let newExercise = {title: null, notes: [], sets: []};
    workout.exercises.push(newExercise);
    const exerciseNo = workout.exercises.length - 1;
    const short = 'exercise' + exerciseNo;
    let newExerciseHTML = document.createElement('div');
    newExerciseHTML.setAttribute('id', short + '-container');
    newExerciseHTML.innerHTML = '<br><label for="' + short + '">Exercise #' + (exerciseNo + 1) +
        ':</label><div><span contenteditable="true" class="my-input" id="' + short +
        '"></span><button class="my-button handwriting" onclick="addNote(' + exerciseNo +
        ');">&nbsp</button></div><div id="' + short + '-notes"></div><br><div id="' + short +
        '-sets"></div><button class="my-button" onclick="addSet(' + exerciseNo +
        ');">Add set</button><br>';
    document.getElementById("exercises").appendChild(newExerciseHTML);
    document.getElementById(short).setAttribute('data-set',
        '["exercises","' + exerciseNo + '","title"]');
    document.getElementById(short).focus();
}

function addSet(exerciseNo) {
    let newSet = {data: null, notes: []};
    workout.exercises[exerciseNo].sets.push(newSet);
    let setNo = workout.exercises[exerciseNo].sets.length - 1;
    const short = "exercise" + exerciseNo + "set" + setNo;
    let newSetHTML = document.createElement('div');
    newSetHTML.setAttribute('id', short + '-container');
    newSetHTML.innerHTML = '<br><label for="' + short + '">Set #' + (setNo + 1) + ':</label>' +
        '<span contenteditable="true" class="my-input" id="' + short + '"></span><button ' +
        'class="my-button handwriting" onclick="addNote(' + exerciseNo + ', ' + setNo + ');"' +
        '>&nbsp</button><div id="' + short + '-notes"></div><br>';
    console.log("exercise" + exerciseNo + "-sets");
    document.getElementById("exercise" + exerciseNo + "-sets").appendChild(newSetHTML);
    document.getElementById(short).setAttribute('data-set',
        '["exercises","' + exerciseNo + '","sets","' + setNo + '","data"]');
    document.getElementById(short).focus();
}

function addNote() {
    let newNote = {type: 0, content: null};
    let noteNo, short, dataSetContent = null;
    let appendHere = document.getElementById('notes');
    switch (arguments.length) {
        case 0:
            workout.notes.push(newNote);
            noteNo = workout.notes.length - 1;
            short = "note" + noteNo;
            dataSetContent = '["notes","' + noteNo + '","content"]';
            break;
        case 1:
            workout.exercises[arguments[0]].notes.push(newNote);
            noteNo = workout.exercises[arguments[0]].notes.length - 1;
            short = "exercise" + arguments[0] + "note" + noteNo;
            appendHere = document.getElementById("exercise" + arguments[0] + "-notes");
            dataSetContent = '["exercises","' + arguments[0] + '","notes","' + noteNo +
                '","content"]';
            break;
        case 2:
            workout.exercises[arguments[0]].sets[arguments[1]].notes.push(newNote);
            noteNo = workout.exercises[arguments[0]].sets[arguments[1]].notes.length - 1;
            short = "exercise" + arguments[0] + "set" + arguments[1] + "note" + noteNo;
            appendHere = document.getElementById(
                "exercise" + arguments[0] + "set" + arguments[1] + "-notes");
            dataSetContent = '["exercises","' + arguments[0] + '","sets","' + arguments[1] +
                '","notes","' + noteNo + '","content"]';
            break;
    }
    let newNoteHTML = document.createElement('div');
    newNoteHTML.setAttribute('id', short + '-container');
    newNoteHTML.innerHTML = '<label for="' + short + '">Note #' + (noteNo + 1) + ':</label>' +
        '<span contenteditable="true" class="my-input" id="' + short + '"></span>' +
        '<select onchange="changeNoteType(this.value, \'' + short + '\');"' +
        ' name="' + short + '-type"><option value="0">Text</option><option value="1">' +
        'Audio</option><option value="2">Picture</option><option value="3">Video</option></select>';
    appendHere.appendChild(newNoteHTML);
    document.getElementById(short).setAttribute('data-set', dataSetContent);
    document.getElementById(short).focus();
}

function changeNoteType(selectFieldValue, fieldToReplaceID) {
    let replacement = document.createElement('input');
    replacement.setAttribute('type', 'file');
    switch (selectFieldValue) {
        case '0':
            replacement = document.createElement('span');
            replacement.className = 'my-input';
            replacement.setAttribute('contenteditable', 'true');
            replacement.setAttribute('type', 'text');
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
    replacement.setAttribute('id', fieldToReplaceID);
    replacement.onchange = function(){
        workout[fieldToReplaceID + '.content'] = this.files[0].name;
        workout[fieldToReplaceID + '.type'] = selectFieldValue;
        uploadFile(this.files[0]);
    };
    fieldToReplaceNode.parentElement.replaceChild(replacement, fieldToReplaceNode);
}

function uploadFile(file) {
    let reader = new FileReader();
    reader.onloadend = function (event) {
        let currentEvent = event.target;
        if (currentEvent.readyState === FileReader.DONE) {
            files.push(reader.result);
        }
    };
    reader.readAsBinaryString(file);
}

function saveWorkout(workout) {
    // Obtain CSRF token
    let token = $("meta[name='_csrf']").attr("content");
    // Send workout object in JSON format
    $.ajax({
        url: window.location.href,
        headers: {"X-CSRF-TOKEN": token},
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(workout),
        async: true,
    }).done(function () {
        window.location.pathname = 'wl/user'
    })
        .fail(function () {
            alert('There\'s been a problem!')
        });
}