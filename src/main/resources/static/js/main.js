/*****************************************************************
 * Functions and variables related to page navigation and layout *
 *****************************************************************/

let OkToToggleLogo = true; // Global flag for logo visibility toggling

$(document).ready(function () {

    /** This event listener is responsible for hiding the logo container and
     * making the tab navigation bar stick to the top of the screen when user is
     * scrolling down the page and showing the logo container and unsticking the
     * navigation bar when the page is scrolled back to the very top. It has to be
     * coordinated with switching tabs, which automatically scrolls the page to
     * the very top.*/

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

/** This function utilizes Split plugin to create two flexible columns with adjustable width.
 */
function split() {
    Split(["#workout-content-container", "#workout-selection-container"], {
        elementStyle: function (dimension, size, gutterSize) {
            $(window).trigger('resize'); // Optional
            return {'flex-basis': 'calc(' + size + '% - ' + gutterSize + 'px)'}
        },
        gutterStyle: function (dimension, gutterSize) {
            return {'flex-basis': gutterSize + 'px'}
        },
        sizes: [74, 24],
        minSize: [150, 80],
        gutterSize: 6,
        cursor: 'col-resize'
    });
}

/** This function updates a single lift section in General Strength tab whenever there's an input
 *  from user by entering a value or moving the slider. The other input gets updated (slider or
 *  input field respectively) and new max is calculated based on weight and reps fields.
 *  @param {string} fieldToUpdate   id of the other element to update
 *  @param {number}  value   value that fieldToUpdate will be updated with
 *  @param {string} weightField id of the element that stores the weight
 *  @param {string} repsField   id of the element that stores the number of reps
 *  @param {string} maxField    id of the element that will be updated with calculation result
 */
function updateGS(fieldToUpdate, value, weightField, repsField, maxField) {
    document.getElementById(fieldToUpdate).value = value;
    let weight = document.getElementById(weightField).value;
    let reps = document.getElementById(repsField).value;
    document.getElementById(maxField).value = Math.round(weight * (1 + (reps / 30)) * 100) / 100;
}

/** This function updates an element of certain id with given value.
 * @param {string}  field   id of the element to update
 * @param {number}  value   value the element will be updated with
 */
function update(field, value) {
    document.getElementById(field).value = value;
}

/** This function calculates the 1 rep max and percentages in 1 Rep Max tab.
 */
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

/** This function updates descriptions for percentages of 1 Rep Max.
 * @param {string}  description id of the description to be updated
 * @param {number}  percentage  the current value of percentage
 */
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

/** This initializes a creation of a new workout in the left panel.
 */
function addWorkout() {
    // Create an empty workout object
    workout = {
        id: 0, title: null, created: null, updated: null, user: null,
        notes: [], exercises: []
    };
    // Update "created" entry with current timestamp
    const created = new Date().toISOString().slice(0, 19).replace('T', ' ');
    workout['created'] = created;
    document.getElementById("created").value = created.slice(0, 16);
    // Continue with workout display and edition
    displayWorkout();
}

/** This initializes the edition of an existing workout in the left panel.
 */
function editWorkout() {
    // Get existing workout object from session storage and update "created" and "title" entries
    workout = JSON.parse(sessionStorage.getItem('workout'));
    document.getElementById("created").value = workout['created'].slice(0, 16);
    document.getElementById("title").innerHTML = workout['title'];
    // Update the "updated" entry with current timestamp
    const updated = new Date().toISOString().slice(0, 19).replace('T', ' ');
    workout['updated'] = updated;
    document.getElementById("updated").value = updated.slice(0, 16);
    // Continue with workout display and edition
    displayWorkout();
}

/** This displays existing workout elements (if there are any) and allows the user to add,
 *  delete and edit any of them.
 */
function displayWorkout() {
    /* This event listener is responsible for transferring values from content editable span
     elements marked with "my-input" class to workout object entries. Values are extracted from
     innerHTML properties. Workout entries are stored in custom data attributes of said elements */
    document.getElementsByClassName('workout-content')[0]
        .addEventListener('input', function (event) {
            // equalize column height in case it changed
            document.getElementById('workout-selection-container').style.height =
                document.getElementsByClassName('workout-content')[0].offsetHeight + "px";
            // store value in workout object entry
            const elementHoldingValue = event.target;
            if (elementHoldingValue.className === 'my-input') {
                let workoutEntry = $(elementHoldingValue).data('set').split(',');
                storeInWorkout(workoutEntry, elementHoldingValue.innerHTML);
            }
        });
    // Display existing workout notes
    for (let i = 0; i < workout.notes.length; i++) {
        addNote(i, workout.notes[i].type, workout.notes[i].content);
    }
    // Display existing exercises
    for (let j = 0; j < workout.exercises.length; j++) {
        addExercise(j, workout.exercises[j].title);
        // Display existing exercise notes
        for (let k = 0; k < workout.exercises[j].notes.length; k++) {
            addNote(k, workout.exercises[j].notes[k].type, workout.exercises[j].notes[k].content, j);
        }
        // Display existing exercise sets
        for (let l = 0; l < workout.exercises[j].sets.length; l++) {
            addSet(j, l, workout.exercises[j].sets[l].data);
            // Display existing set notes
            for (let m = 0; m < workout.exercises[j].sets[l].notes.length; m++) {
                addNote(m, workout.exercises[j].sets[l].notes[m].type,
                    workout.exercises[j].sets[l].notes[m].content, j, l);
            }
        }
    }
}

/** Stores given value in workout object entry.
 * @param entry {string[]}  workout object entry name
 * @param value {string}    text to be stored in workout entry
 */
function storeInWorkout(entry, value) {
    // remove last <br> (these often get stuck in content editable elements)
    value = value.replace(/^\s*<br\s*\/?>|<br\s*\/?>\s*$/g, '');
    // store the text in workout object entry using bracket notation
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

/** Adds exercise to workout.
 * @param {number} [exerciseNo] - exercise number (only when editing existing workout)
 * @param {string} [title] - exercise title (only when editing existing workout)
 */
function addExercise(exerciseNo = undefined, title = undefined) {
    // add workout object entry if it's a new exercise
    if (exerciseNo === undefined) {
        let newExercise = {title: null, notes: [], sets: []};
        workout.exercises.push(newExercise);
        exerciseNo = workout.exercises.length - 1;
    }
    // create an element, it's innerHTML and set it's attributes
    const short = 'exercise' + exerciseNo;
    let newExerciseHTML = document.createElement('div');
    newExerciseHTML.setAttribute('id', short + '-container');
    newExerciseHTML.innerHTML = '<br><label for="' + short + '">Exercise #' +
        (exerciseNo + 1) + ': </label><span contenteditable="true" class="my-input" id="' + short +
        '"></span><button class="my-btn add bn" onclick="addNote(undefined, 0, undefined, ' +
        exerciseNo + ');" title="Add exercise note">&nbsp</button><button class="my-btn del bn"' +
        ' onclick="remove(' + short + ');" title="Delete exercise">&nbsp</button></div><div id="' +
        short + '-notes"></div><br><div id="' + short + '-sets"></div><button class="my-btn" ' +
        'onclick="addSet(' + exerciseNo + ');">Add set</button><br>';
    document.getElementById("exercises").appendChild(newExerciseHTML);
    document.getElementById(short).setAttribute('data-set',
        "exercises," + exerciseNo + ",title");
    // set content if displaying an existing exercise or focus on element if creating a new one
    if (title !== undefined) {
        document.getElementById(short).innerHTML = title;
    } else {
        equalizeColumnHeight();
        document.getElementById(short).focus();
    }
}

/** Adds set to workout.
 * @param {number} [exerciseNo] - exercise number
 * @param {number} [setNo] - set number (only when editing existing workout)
 * @param {string} [data] - set data (only when editing existing workout)
 */
function addSet(exerciseNo, setNo = undefined, data = undefined) {
    // add workout object entry if it's a new set
    if (setNo === undefined) {
        let newSet = {data: null, notes: []};
        workout.exercises[exerciseNo].sets.push(newSet);
        setNo = workout.exercises[exerciseNo].sets.length - 1;
    }
    // create an element, its innerHTML and set its attributes
    const short = "exercise" + exerciseNo + "set" + setNo;
    let newSetHTML = document.createElement('div');
    newSetHTML.setAttribute('id', short + '-container');
    newSetHTML.innerHTML = '<br><label for="' + short + '">Set #' +
        (setNo + 1) + ': </label><span contenteditable="true" class="my-input" ' +
        'id="' + short + '"></span><button class="my-btn add bn" ' +
        'onclick="addNote(undefined, 0, undefined, ' + exerciseNo + ', ' + setNo + ');"' +
        ' title="Add set note">&nbsp</button><button class="my-btn del bn" ' +
        'onclick="remove(' + short + ');" title="Delete set">&nbsp</button>' +
        '<div id="' + short + '-notes"></div><br>';
    document.getElementById("exercise" + exerciseNo + "-sets").appendChild(newSetHTML);
    document.getElementById(short).setAttribute('data-set',
        "exercises," + exerciseNo + ",sets," + setNo + ",data");
    // set content if displaying an existing set or focus on element if creating a new one
    if (data !== undefined) {
        document.getElementById(short).innerHTML = data;
    } else {
        equalizeColumnHeight();
        document.getElementById(short).focus();
    }
}

/** Adds a note to workout, exercise or set - depending on parameters. It can be a fresh (empty)
 * note added by user during workout creation / edition or already existing note when displaying an
 * existing workout.
 * @param {number} noteNo - the index in notes array (undefined when it's a fresh note)
 * @param {number} type - text (0), audio (1), picture (2), video (3)
 * @param {string} content - either text content of the text note or a filename for any other type
 * @param {number} [exerciseNo] - optional number of exercise note is assigned to
 * @param {number} [setNo] - optional number of set note is assigned to
 */
function addNote(noteNo, type, content, exerciseNo = undefined, setNo = undefined) {
    // is it a new note?
    const itsANewNote = (noteNo === undefined);
    // assume it's a workout note (to avoid code repetition)
    let newNote = {type: type, content: content};
    let noteListAlias = workout.notes;
    let appendHere = document.getElementById('notes');
    if (itsANewNote) {
        noteNo = workout.notes.length;
    }
    let short = "note" + noteNo;
    let dataSetContent = "notes," + noteNo + ",content";
    // if it's an exercise note - modify values
    if (exerciseNo !== undefined && setNo === undefined) {
        noteListAlias = workout.exercises[exerciseNo].notes;
        appendHere = document.getElementById("exercise" + exerciseNo + "-notes");
        if (itsANewNote) {
            noteNo = workout.exercises[exerciseNo].notes.length;
        }
        short = "exercise" + exerciseNo + "note" + noteNo;
        dataSetContent = "exercises," + exerciseNo + ",notes," + noteNo + ",content";
    }
    // it's a set note - modify values
    if (exerciseNo !== undefined && setNo !== undefined) {
        noteListAlias = workout.exercises[exerciseNo].sets[setNo].notes;
        appendHere = document.getElementById(
            "exercise" + exerciseNo + "set" + setNo + "-notes");
        if (itsANewNote) {
            noteNo = workout.exercises[exerciseNo].sets[setNo].notes.length;
        }
        short = "exercise" + exerciseNo + "set" + setNo + "note" + noteNo;
        dataSetContent = "exercises," + exerciseNo + ",sets," + setNo + ",notes," +
            noteNo + ",content";
    }
    // create an element, innerHTML and set attributes
    let newNoteHTML = document.createElement('div');
    newNoteHTML.setAttribute('id', short + '-container');
    newNoteHTML.innerHTML = '<label for="' + short + '">Note #' + (noteNo + 1) +
        ': </label><span contenteditable="true" class="my-input" id="' + short + '"></span>' +
        '<select onchange="changeNoteType(this.value, \'' + short + '\');"' +
        ' name="' + short + '-type"><option value="0">Text</option><option value="1">' +
        'Audio</option><option value="2">Picture</option><option value="3">Video</option>' +
        '</select><button class="my-btn del bn" onclick="remove(' + short + ');" ' +
        'title="Delete note">&nbsp</button>';
    appendHere.appendChild(newNoteHTML);
    document.getElementById(short).setAttribute('data-set', dataSetContent);
    // if new note - push it to note array and focus on element
    if (itsANewNote) {
        noteListAlias.push(newNote);
        document.getElementById(short).focus();
        equalizeColumnHeight();
        // if not - display the note content (and change type if other than text note)
    } else {
        if (newNote.type !== 0) {
            displayExistingMediaNote(short, newNote.content, newNote.type);
        } else {
            document.getElementById(short).innerHTML = newNote.content;
        }
    }
}

/** Changes a note type when user chooses a different one using the select element.
 * @param {number} selectFieldValue - 0 for text, 1 for audio, 2 for picture, 3 for video
 * @param {string} fieldToReplaceId - id of note node
 */
function changeNoteType(selectFieldValue, fieldToReplaceId) {
    // clear note content and set new note type in corresponding workout object entries
    setNoteContentAndType(document.getElementById(fieldToReplaceId), '', selectFieldValue);
    // assume that new note is a media note (to avoid a lot of duplicate code below)
    let replacement = document.createElement('input');
    replacement.setAttribute('type', 'file');
    replacement.onchange = function () {
        setNoteContentAndType(this, this.files[0].name, selectFieldValue);
        attachFile(this.id, this.files[0], this.files[0].name, selectFieldValue);
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
    document.getElementById(fieldToReplaceId).setAttribute('id', 'tempID');
    let fieldToReplaceNode = document.getElementById('tempID');
    replacement.dataset.set = fieldToReplaceNode.dataset.set;
    replacement.setAttribute('id', fieldToReplaceId);
    fieldToReplaceNode.parentElement.replaceChild(replacement, fieldToReplaceNode);
    equalizeColumnHeight();
}

/** Auxiliary function used to set corresponding entries in workout object when user changes the
 * type of note.
 * @param {Node} noteId - note HTML node
 * @param {string} content - content of note (text of filename)
 * @param type - type of note (text(0), audio(1), picture(2), video(3))
 */
function setNoteContentAndType(noteId, content, type) {
    let workoutEntry = $(noteId).data('set').split(',');
    storeInWorkout(workoutEntry, content);
    workoutEntry[workoutEntry.length - 1] = "type";
    storeInWorkout(workoutEntry, type);
}

/** Stores file attached to a media note in files array, removes input field, shows filename
 *  and play button.
 * @param {string} fileInputId - file input field Id
 * @param {Blob} file - file to upload
 * @param {string} content - media note content (a filename)
 * @param {number} noteType - type of note (text(0), audio(1), picture(2), video(3))
 */
function attachFile(fileInputId, file, content, noteType) {
    let reader = new FileReader();
    reader.onloadend = function (event) {
        let currentEvent = event.target;
        if (currentEvent.readyState === FileReader.DONE) {
            files.push(reader.result);
        }
        displayExistingMediaNote(fileInputId, content, noteType);
    };
    reader.readAsDataURL(file);
}

/** This displays a new span element with filename in place of file select element after user
 * upload the media file. It also replaces note type select with media play button.
 * @param {string } noteId - id of note element
 * @param {string} content - note content (filename)
 * @param {number} type - media note type -  audio(1), picture(2), video(3)
 */
function displayExistingMediaNote(noteId, content, type) {
    const oldNote = document.getElementById(noteId);
    const data = oldNote.dataset.set;
    $(oldNote).replaceWith('<span id="' + noteId + '" class="my-input" data-set="'
        + data + '">' + content + '</span>');
    const newNote = document.getElementById(noteId);
    switch (type) {
        case 1:
        case "1":
            $(newNote).next().replaceWith('<button class="my-btn audio bn"' +
                ' title="play audio" onclick="play();">&nbsp</button>');
            break;
        case 2:
        case "2":
            $(newNote).next().replaceWith('<button class="my-btn image bn"' +
                ' title="show picture" onclick="play();">&nbsp</button>');
            break;
        case 3:
        case "3":
            $(newNote).next().replaceWith('<button class="my-btn clip bn"' +
                ' title="play video" onclick="play();">&nbsp</button>');
            break;
    }
}

function play() {
    alert('playing!');
}

/** Removes element from workout and corresponding entry in workout object. Used to remove
 *  exercise, set or note of any kind from workout
 *  @param {Node} [element] - element to remove
 */
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
    document.getElementById('notes').innerHTML = '';
    document.getElementById('exercises').innerHTML = '';
    displayWorkout();
}

/** Temporary solution for workout selection window getting too short when editing workout in
 * the right pane
 */
function equalizeColumnHeight() {
document.getElementById('workout-selection-container').style.height =
    document.getElementsByClassName('workout-content')[0].offsetHeight + "px";
}

/** Gets existing workout details in JSON format from REST controller.
 * @param {number} [workoutId] - id number of workout to be loaded from database */
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
        window.location.pathname = 'wl/workout/';
    })
        .fail(function () {
            alert('There\'s been a problem loading the workout data!')
        });
}

/** Sends new workout object in JSON format to REST controller using POST AJAX call.
 * @param {object} [workout] - workout object to be persisted */
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
            alert('There\'s been a problem saving the workout data!')
        });
}

/** Request deletion of the currently displayed workout from the database. */
function deleteWorkout() {
    // Obtain CSRF token
    let token = $("meta[name='_csrf']").attr("content");
    // Request workout of given id to be deleted from database
    $.ajax({
        url: 'http://localhost:8080/wl/workout/' + workout.id,
        headers: {"X-CSRF-TOKEN": token},
        type: 'DELETE',
        async: true,
    }).done(function () {
        window.location.pathname = 'wl/user'
    })
        .fail(function () {
            alert('There\'s been a problem deleting this workout!')
        });
}