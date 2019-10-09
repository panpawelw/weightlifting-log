/****************************************************************
 * Functions and variables related to page navigation and layout*
 ****************************************************************/

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
});

/* Auxiliary function for tab change event handlers */
function scrollToTop() {
    OkToToggleLogo = false;
    $('html').animate({scrollTop: 1}, 'fast');
    OkToToggleLogo = true;
}

/* This function updates a single lift section in General Strength tab
   whenever there's an input from user by entering a value or moving the slider. The other input
    gets updated (slider or input field) and new max is calculated based on weight and reps fields*/
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

/****************************************************************************
 * Functions and variables related to workout data creation an manipulation *
 ****************************************************************************/

let workout = null; // Global workout object variable
let files = []; // Array for storing files attached to media notes (will eventually be removed)

function addWorkout() {
    workout = {
        id: 0, title: null, created: null, updated: null, user: null,
        notes: [], exercises: []
    };
    const created = new Date().toISOString().slice(0, 19).replace('T', ' ');
    document.getElementById("created").value = created;
    workout['created'] = created;
    displayWorkout();
}

function editWorkout() {
    workout = JSON.parse(sessionStorage.getItem('workout'));
    document.getElementById("created").value = workout['created'];
    document.getElementById("title").innerHTML = workout['title'];
    const updated = new Date().toISOString().slice(0, 19).replace('T', ' ');
    document.getElementById("updated").value = updated;
    workout['updated'] = updated;
    displayWorkout();
}

function displayWorkout() {
    /* This event listener is responsible for transferring values from content editable span
     elements marked with "my-input" class to workout object entries. Values are extracted from
     innerHTML properties. Workout entries are stored in custom data attributes of said elements */
    document.getElementsByClassName('workout-content')[0]
        .addEventListener('input', function (event) {
            const elementHoldingValue = event.target;
            if (elementHoldingValue.className === 'my-input') {
                let workoutEntry = $(elementHoldingValue).data('set').split(',');
                storeInWorkout(workoutEntry, elementHoldingValue.innerHTML);
            }
        });
    for (let i = 0; i < workout.exercises.length; i++) {
        addExercise(i, workout.exercises[i].title);
        for (let j = 0; j < workout.exercises[i].sets.length; j++) {
            addSet(i,j,workout.exercises[i].sets[j].data);
        }
    }
}

/* Stores given value in workout object entry. Last <br> in value is removed (single <br> values
 tend to get stuck in contenteditable fields) and the rest are changed into \n symbols. The entry
 name is constructed from string array using bracket notation */
function storeInWorkout(entry, value) {
    value = value.replace(/^\s*<br\s*\/?>|<br\s*\/?>\s*$/g, '');
    value = value.replace(/<br\s*[\/]?>/gi, "\n");
    switch (entry.length) {
        case 1:
            workout[entry[0]] = value;
            break;
        case 3:
            workout[entry[0]][entry[1]][entry[2]] = value;
            break;
        case 5:
            workout[entry[0]][entry[1]][entry[2]][entry[3]][entry[4]] = value;
            break;
        case 7:
            workout[entry[0]][entry[1]][entry[2]][entry[3]][entry[4]][entry[5]][entry[6]]
                = value;
            break;
    }
}

function addExercise() {
    let exerciseNo = null;
    if (arguments.length > 0) {
        exerciseNo = arguments[0];
    } else {
        let newExercise = {title: null, notes: [], sets: []};
        workout.exercises.push(newExercise);
        exerciseNo = workout.exercises.length - 1;
    }
    const short = 'exercise' + exerciseNo;
    let newExerciseHTML = document.createElement('div');
    newExerciseHTML.setAttribute('id', short + '-container');
    newExerciseHTML.innerHTML = '<br><label for="' + short + '">Exercise #' + (exerciseNo + 1) +
        ': </label><span contenteditable="true" class="my-input" id="' + short +
        '"></span><button class="my-button add-note" onclick="addNote(0, null, ' + exerciseNo +
        ');" title="Add exercise note">&nbsp</button><button class="my-button remove"' +
        ' onclick="remove(' + short + ');" title="Delete exercise">&nbsp</button><div id="' +
        short + '-notes"></div><br><div id="' + short + '-sets"></div><button class="my-button" ' +
        'onclick="addSet(' + exerciseNo + ');">Add set</button><br>';
    document.getElementById("exercises").appendChild(newExerciseHTML);
    document.getElementById(short).setAttribute('data-set',
        "exercises," + exerciseNo + ",title");
    if (arguments.length > 0) {
        document.getElementById(short).innerHTML = arguments[1];
    } else {
        document.getElementById(short).focus();
    }
}

function addSet(exerciseNo) {
    let setNo = null;
    if (arguments.length > 1) {
        setNo = arguments[1];
    } else {
        let newSet = {data: null, notes: []};
        workout.exercises[exerciseNo].sets.push(newSet);
        setNo = workout.exercises[exerciseNo].sets.length - 1;
    }
    const short = "exercise" + exerciseNo + "set" + setNo;
    let newSetHTML = document.createElement('div');
    newSetHTML.setAttribute('id', short + '-container');
    newSetHTML.innerHTML = '<br><label for="' + short + '">Set #' + (setNo + 1) + ': </label>' +
        '<span contenteditable="true" class="my-input" id="' + short + '"></span><button ' +
        'class="my-button add-note" onclick="addNote(0, null, ' + exerciseNo + ', ' + setNo + ');"' +
        ' title="Add set note">&nbsp</button><button class="my-button remove" ' +
        'onclick="remove(' + short + ');" title="Delete set">&nbsp</button>' +
        '<div id="' + short + '-notes"></div><br>';
    document.getElementById("exercise" + exerciseNo + "-sets").appendChild(newSetHTML);
    document.getElementById(short).setAttribute('data-set',
        "exercises," + exerciseNo + ",sets," + setNo + ",data");
    if (arguments.length > 1) {
        document.getElementById(short).innerHTML = arguments[2];
    } else {
        document.getElementById(short).focus();
    }
}

function addNote(type, content) {
    let newNote = {type: type, content: content};
    let appendHere = document.getElementById('notes');
    let noteNo, short, dataSetContent, noteListAlias = null;
    switch (arguments.length) {
        case 3:
            noteNo = workout.exercises[arguments[2]].notes.length;
            noteListAlias = workout.exercises[arguments[2]].notes;
            short = "exercise" + arguments[2] + "note" + noteNo;
            appendHere = document.getElementById("exercise" + arguments[2] + "-notes");
            dataSetContent = "exercises," + arguments[2] + ",notes," + noteNo + ",content";
            break;
        case 4:
            noteNo = workout.exercises[arguments[2]].sets[arguments[3]].notes.length;
            noteListAlias = workout.exercises[arguments[2]].sets[arguments[3]].notes;
            short = "exercise" + arguments[2] + "set" + arguments[3] + "note" + noteNo;
            appendHere = document.getElementById(
                "exercise" + arguments[2] + "set" + arguments[3] + "-notes");
            dataSetContent = "exercises," + arguments[2] + ",sets," + arguments[3] + ",notes," +
                noteNo + ",content";
            break;
        default:
            noteNo = workout.notes.length;
            short = "note" + noteNo;
            dataSetContent = "notes," + noteNo + ",content";
            noteListAlias = workout.notes;
    }
    if (newNote.content == null) {
        newNote.content = '';
        noteListAlias.push(newNote);
    }
    let newNoteHTML = document.createElement('div');
    newNoteHTML.setAttribute('id', short + '-container');
    newNoteHTML.innerHTML = '<label for="' + short + '">Note #' + (noteNo + 1) + ': </label>' +
        '<span contenteditable="true" class="my-input" id="' + short + '"></span>' +
        '<select onchange="changeNoteType(this.value, \'' + short + '\');"' +
        ' name="' + short + '-type"><option value="0">Text</option><option value="1">' +
        'Audio</option><option value="2">Picture</option><option value="3">Video</option>' +
        '</select><button class="my-button remove" onclick="remove(' + short + ');" ' +
        'title="Delete note">&nbsp</button>';
    appendHere.appendChild(newNoteHTML);
    document.getElementById(short).setAttribute('data-set', dataSetContent);
    document.getElementById(short).focus();
}

/* Changes a note type when user chooses a different one using the select element.
 SelectFieldValue is 0 for text, 1 for audio, 2 for picture, 3 for video, fieldToReplaceId is a
  note field ID as string.*/
function changeNoteType(selectFieldValue, fieldToReplaceID) {
    // clear note content and set new note type in corresponding workout object entries
    setNoteContentAndType(document.getElementById(fieldToReplaceID), '', selectFieldValue);
    // assume that new note is a media note (to avoid a lot of duplicate code below)
    let replacement = document.createElement('input');
    replacement.setAttribute('type', 'file');
    replacement.onchange = function () {
        setNoteContentAndType(this, this.files[0].name, selectFieldValue);
        uploadFile(this.files[0]);
    };
    switch (selectFieldValue) {
        case '0':
            // if new note is a text note after all - change HTML element type and attributes
            replacement = document.createElement('span');
            replacement.className = 'my-input';
            replacement.setAttribute('contenteditable', 'true');
            replacement.removeAttribute('onchange');
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
    // switch IDs of old and new elements, copy data and replace
    document.getElementById(fieldToReplaceID).setAttribute('id', 'tempID');
    let fieldToReplaceNode = document.getElementById('tempID');
    replacement.dataset.set = fieldToReplaceNode.dataset.set;
    replacement.setAttribute('id', fieldToReplaceID);
    fieldToReplaceNode.parentElement.replaceChild(replacement, fieldToReplaceNode);
}

/* Auxiliary function used to set corresponding entries in workout object when user changes the
 type of a note. Workout entries are identified using custom dataset attached to HTML note element */
function setNoteContentAndType(noteId, content, type) {
    let workoutEntry = $(noteId).data('set').split(',');
    storeInWorkout(workoutEntry, content);
    workoutEntry[workoutEntry.length - 1] = "type";
    storeInWorkout(workoutEntry, type);
}

/* Stores file attached to a media note in files array */
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

/** Removes element from workout and corresponding entry in workout object.
 * Used to remove exercise, set or note of any kind from workout */
function remove(element) {
    const entry = $(element).data('set').split(',');
    const parent = element.parentElement;
    parent.parentElement.removeChild(parent);
    switch (entry.length) {
        case 3:
            workout[entry[0]].splice(entry[1], 1);
            break;
        case 5:
            workout[entry[0]][entry[1]][entry[2]].splice(entry[3], 1);
            break;
        case 7:
            workout[entry[0]][entry[1]][entry[2]][entry[3]][entry[4]].splice(entry[5], 1);
            break;
    }
}

/** Gets existing workout details in JSON format from REST controller.
 * @param {int} workoutId   id of workout to be loaded from database */
function loadWorkout(workoutId) {
    // Obtain CSRF token
    let token = $("meta[name='_csrf']").attr("content");
    // Request workout object of given id in JSON format
    $.ajax({
        url: 'http://localhost:8080/wl/workout/' + workoutId,
        headers: {"X-CSRF-TOKEN": token},
        type: 'GET',
        dataType: 'JSON',
        async: true,
    }).done(function (data) {
        sessionStorage.setItem('workout', JSON.stringify(data));
        window.location.pathname = 'wl/workout/details';
    })
        .fail(function () {
            alert('There\'s been a problem!')
        });
}

/** Sends new workout object in JSON format to REST controller using POST AJAX call.
 * @param {object}  workout  workout object to be persisted */
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

/** Request deletion of the currently displayed workout from the database. */
function deleteWorkout() {
    // Obtain CSRF token
    let token = $("meta[name='_csrf']").attr("content");
    // Request workout of given id to be deleted from
    $.ajax({
        url: 'http://localhost:8080/wl/workout/' + workout.id,
        headers: {"X-CSRF-TOKEN": token},
        type: 'DELETE',
        async: true,
    }).done(function () {
        window.location.pathname = 'wl/user'
    })
        .fail(function () {
            alert('There\'s been a problem!')
        });
}