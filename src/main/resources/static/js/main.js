/*****************************************************************
 * Functions and variables related to page navigation and layout *
 *****************************************************************/

let okToToggleLogo = true; // Global flag for logo visibility toggling
let BASE_URL;

$(function () {

  getBaseUrl();

  $("#workout-content-container").resizable({
    handleSelector: ".splitter",
    resizeHeight: false
  });

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
    const pagePosition = $('html').scrollTop();

    // Is it OK to toggle logo visibility?
    if (okToToggleLogo) {

      // Is the page scrolled down and is logo visible? Hide logo.
      if (pagePosition !== 0 && pagePosition !== 1 && logo.style.display !== 'none') {

        navbar.classList.add('sticky');
        filler1.classList.add('sticky');
        $(logo).slideUp(100).queue(false);
        $(filler2).show(100).queue(false);
        $(filler1).show(100).queue(false);

        // Is the page scrolled up and is logo hidden? Show logo.
      } else if (pagePosition === 0 && logo.style.display === 'none') {

        navbar.classList.remove('sticky');
        filler1.classList.remove('sticky');
        $(filler1).slideUp(100).queue(false);
        $(filler2).slideUp(100).queue(false);
        $(logo).show(100).queue(false);
      }

      /* If logo toggling is not allowed, but the page is scrolled
       all the way up, allow logo toggling next time. */
    } else if (pagePosition === 0) {
      okToToggleLogo = true;
    }
  });

  /* These handlers are responsible for scrolling the document to the top
  when user is changing tabs, while not allowing the logo toggle action to be
  triggered by the scrolling. */
  $('#tab1handle, #tab2handle, #tab3handle, #tab4handle').on('click', function () {
    okToToggleLogo = false;
    $('html').animate({scrollTop: 1}, {
      duration: 250, complete: function () {
        okToToggleLogo = true;
      }
    });
  });
});

/** This stores the application context url and puts it in session storage
 */
function getBaseUrl() {
  BASE_URL = sessionStorage.getItem('BASE_URL');
  if (BASE_URL === undefined || BASE_URL === null) {
    sessionStorage.setItem('BASE_URL', window.location.href);
  }
}

/** This stores the state of logo in session storage so it can be preserved when page is reloaded.
 */
function storeLogoState() {
  sessionStorage.setItem('logoVisibility',
    document.getElementById('logo-container').style.display);
}

/** This restores the state of logo visibility
 */
function restoreLogoState() {
  // Prevent logo toggling and move screen content to the top
  okToToggleLogo = false;
  document.getElementsByTagName('html')[0].scrollTop = 1;
  setTimeout(
    // Allow logo toggling again after a short delay
    function () {
      okToToggleLogo = true;
    }, 333);
  // Hide logo if it was previously hidden
  let logoVisibility = sessionStorage.getItem('logoVisibility');
  sessionStorage.removeItem('logoVisibility');
  if (logoVisibility === 'none') {
    document.getElementById('big-tabs').classList.add('sticky');
    document.getElementById('filler-1').classList.add('sticky');
    $(document.getElementById('logo-container')).hide().queue(false);
    $(document.getElementById('filler-2')).show().queue(false);
    $(document.getElementById('filler-1')).show().queue(false);
  }
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
function updateGS(fieldToUpdate, value, weightField,
                  repsField, maxField) {
  document.getElementById(fieldToUpdate).value = value;
  const weight = document.getElementById(weightField).value;
  const reps = document.getElementById(repsField).value;
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
  const weight = document.getElementById('weight-text').value;
  const reps = document.getElementById('reps-text').value;
  const result = weight * (1 + (reps / 30));
  document.getElementById('result').value = result.toFixed(2);

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
  let labelNumber = 5;
  const thresholds = [21, 41, 61, 81, 91];
  const labels = [
    "Upper body ballistic work, lower body plyometrics. Improves muscle hardness and contraction " +
    "speed.",
    "Lower body ballistic work. Improves muscle hardness, develops power and contraction speed.",
    "Explosiveness, speed and power. Great for muscle hardness. 60% can be used for hypertrophy " +
    "when done to failure.",
    "Best range for muscle mass building, also good for explosiveness (~70%) and strength (~80%) " +
    "in Olympic lifting.",
    "Best for building muscle strength, use ~80% for easy recovery, ~90% for quick strength " +
    "peaking.",
    "Increases maximal strength via neural factors. Good for displaying " +
    "strength. Difficult to recover from."];

  for (let i = 0; i < 5; i++) {
    if (percentage < thresholds[i]) {
      labelNumber = i;
      break;
    }
  }
  document.getElementById(description).innerHTML = labels[labelNumber];
}

/****************************************************************************
 * Functions and variables related to workout data creation an manipulation *
 ****************************************************************************/

let workout = null;
let originalWorkout = null;
let filesToRemove = [];
let filesToUpload = [];

/** This initializes a creation of a new workout in the left panel.
 */
function addWorkout() {
  // Create an empty workout object
  workout = {
    id: 0, title: null, created: null, updated: null, user: null,
    notes: [], exercises: [], filenames: []
  };
  originalWorkout = {...workout};
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
  originalWorkout = {...workout};
  document.getElementById("created").value = workout['created'].slice(0, 16);
  document.getElementById("title").innerHTML = workout['title'];
  document.getElementById("updated").value = workout['updated'].slice(0, 16);
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
      // document.getElementById('workout-selection-container').style.height =
      //     document.getElementsByClassName('workout-content')[0].offsetHeight + "px";
      // store value in workout object entry
      this.target = event.target;
      if (this.target.className === 'my-input') {
        storeInWorkout($(this.target).data('set').split(','),
          $("<textarea/>").html(this.target.innerHTML).text());
      }
    });

  /** Observer to keep columns equal when workout content length changes.
   */
  const heightObserver = new ResizeObserver(entries => {
    for (let entry of entries) {
      const height = entry.target.offsetHeight + 'px';
      document.getElementById('workout-selection-container').style.height = height;
      document.getElementsByClassName('splitter')[0].style.height = height;
    }
  });
  heightObserver.observe(document.getElementsByClassName('workout-content')[0]);

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
 * @param {string[]} entry - workout object entry name
 * @param {string} value - text to be stored in workout entry
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
      workout[entry[0]][entry[1]][entry[2]][entry[3]][entry[4]][entry[5]][entry[6]] = value;
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
    workout.exercises.push({title: null, notes: [], sets: []});
    exerciseNo = workout.exercises.length - 1;
  }
  // create an element and set it's attributes
  const id = 'exercise' + exerciseNo;
  const newExercise = document.createElement('div');
  newExercise.setAttribute('id', id + '-container');
  newExercise.setAttribute('class', 'bordered');
  const template = document.getElementById('exercise-template');
  newExercise.appendChild(template.content.cloneNode(true));
  newExercise.getElementsByTagName('span')[0].id = id;
  newExercise.getElementsByTagName('label')[0].htmlFor = id;
  newExercise.getElementsByTagName('label')[0].innerHTML = 'Exercise #' + (exerciseNo + 1);
  newExercise.getElementsByClassName('my-btn add bn')[0].onclick =
    function () {
      addNote(undefined, 0, '', exerciseNo);
    };
  newExercise.getElementsByClassName('my-btn del bn')[0].onclick =
    function () {
      remove(document.getElementById(id));
    };
  newExercise.getElementsByClassName('exercise-notes')[0].id = id + '-notes';
  newExercise.getElementsByClassName('exercise-sets')[0].id = id + '-sets';
  newExercise.getElementsByClassName('my-btn add-set')[0]
    .setAttribute('onclick', 'addSet(' + exerciseNo + ');');
  document.getElementById("exercises").appendChild(newExercise);
  document.getElementById(id).setAttribute('data-set',
    "exercises," + exerciseNo + ",title");
  // set content if displaying an existing exercise or focus on element if creating a new one
  if (title === undefined) {
    document.getElementById(id).focus();
  } else {
    document.getElementById(id).innerHTML = title;
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
    workout.exercises[exerciseNo].sets.push({data: null, notes: []});
    setNo = workout.exercises[exerciseNo].sets.length - 1;
  }
  // create an element and set it's attributes
  const id = "exercise" + exerciseNo + "set" + setNo;
  const newSet = document.createElement('div');
  newSet.setAttribute('id', id + '-container');
  newSet.setAttribute('class', 'bordered');
  const template = document.getElementById('set-template');
  newSet.appendChild(template.content.cloneNode(true));
  newSet.getElementsByTagName('span')[0].id = id;
  newSet.getElementsByTagName('label')[0].htmlFor = id;
  newSet.getElementsByTagName('label')[0].innerHTML = 'Set #' + (setNo + 1);
  newSet.getElementsByClassName('my-btn add bn')[0].onclick =
    function () {
      addNote(undefined, 0, '', exerciseNo, setNo);
    };
  newSet.getElementsByClassName('my-btn del bn')[0].onclick =
    function () {
      remove(document.getElementById(id));
    };
  newSet.getElementsByClassName('set-notes')[0].id = id + '-notes';
  document.getElementById("exercise" + exerciseNo + "-sets").appendChild(newSet);
  document.getElementById(id).setAttribute('data-set',
    "exercises," + exerciseNo + ",sets," + setNo + ",data");
  // set content if displaying an existing set or focus on element if creating a new one
  if (data === undefined) {
    document.getElementById(id).focus();
  } else {
    document.getElementById(id).innerHTML = data;
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
function addNote(noteNo, type, content, exerciseNo = undefined,
                 setNo = undefined) {
  // is it a new note?
  const itsANewNote = (noteNo === undefined);
  // assume it's a workout note (to avoid code repetition)
  let newNote = {type: type, content: content};
  let noteListAlias = workout.notes;
  let appendHere = document.getElementById('notes');
  noteNo = itsANewNote ? workout.notes.length : noteNo;
  let id = "note" + noteNo;
  let dataSetContent = "notes," + noteNo + ",content";
  // if it's an exercise note - modify values
  if (exerciseNo !== undefined && setNo === undefined) {
    noteListAlias = workout.exercises[exerciseNo].notes;
    appendHere = document.getElementById("exercise" + exerciseNo + "-notes");
    noteNo = itsANewNote ? workout.exercises[exerciseNo].notes.length : noteNo;
    id = "exercise" + exerciseNo + "note" + noteNo;
    dataSetContent = "exercises," + exerciseNo + ",notes," + noteNo + ",content";
  }
  // it's a set note - modify values
  if (exerciseNo !== undefined && setNo !== undefined) {
    noteListAlias = workout.exercises[exerciseNo].sets[setNo].notes;
    appendHere = document.getElementById(
      "exercise" + exerciseNo + "set" + setNo + "-notes");
    noteNo = itsANewNote ? workout.exercises[exerciseNo].sets[setNo].notes.length : noteNo;
    id = "exercise" + exerciseNo + "set" + setNo + "note" + noteNo;
    dataSetContent = "exercises," + exerciseNo + ",sets," + setNo + ",notes," +
      noteNo + ",content";
  }
  // create an element, innerHTML and set attributes
  let newNoteHTML = document.createElement('div');
  newNoteHTML.setAttribute('id', id + '-container');
  newNoteHTML.setAttribute('class', 'note');
  const template = document.getElementById('note-template');
  newNoteHTML.appendChild(template.content.cloneNode(true));
  newNoteHTML.getElementsByTagName('span')[0].id = id;
  newNoteHTML.getElementsByTagName('label')[0].htmlFor = id;
  newNoteHTML.getElementsByTagName('label')[0].innerHTML = 'Note #' + (noteNo + 1);
  newNoteHTML.getElementsByTagName('select').name = id + '-type';
  newNoteHTML.getElementsByTagName('select')[0].onchange =
    function () {
      changeNoteType(this.value, id);
    };
  newNoteHTML.getElementsByClassName('my-btn del bn')[0].onclick =
    function () {
      remove(document.getElementById(id));
    };

  appendHere.appendChild(newNoteHTML);
  document.getElementById(id).setAttribute('data-set', dataSetContent);
  // if new note - push it to note array and focus on element
  if (itsANewNote) {
    noteListAlias.push(newNote);
    document.getElementById(id).focus();
    // if not - display the note content (and change type if other than text note)
  } else {
    if (newNote.type === 0) {
      document.getElementById(id).innerHTML = newNote.content;
    } else {
      displayExistingMediaNote(id, newNote.content, newNote.type);
      assignFileToExistingMediaNote(id, newNote.content);
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
    attachFile(this.id, this.files[0], selectFieldValue);
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
  }
  // switch IDs of old and new elements, copy data and replace
  document.getElementById(fieldToReplaceId).setAttribute('id', 'tempID');
  const fieldToReplaceNode = document.getElementById('tempID');
  replacement.dataset.set = fieldToReplaceNode.dataset.set;
  replacement.setAttribute('id', fieldToReplaceId);
  fieldToReplaceNode.parentElement.replaceChild(replacement, fieldToReplaceNode);
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

/** This displays a new span element with filename in place of file select element after user
 * upload the media file. It also replaces note type select with media play button and creates
 * the modal element to be displayed when the button is clicked.
 * @param {string} noteId - id of note element
 * @param {string} content - note content (filename)
 * @param {number} type - media note type -  audio(1), picture(2), video(3)
 */
function displayExistingMediaNote(noteId, content, type) {
  const oldNote = document.getElementById(noteId);
  const data = oldNote.dataset.set;
  $(oldNote).replaceWith('<span id="' + noteId + '" class="my-input" data-set="' +
    data + '">' + content + '</span>');
  const newNote = document.getElementById(noteId);
  let modal = document.createElement("div");
  modal.setAttribute('id', noteId + '-modal');
  modal.setAttribute('class', 'modal fade close');
  modal.setAttribute('data-dismiss', 'modal');
  switch (type) {
    case 1:
    case "1":
      $(newNote).next().replaceWith('<button onclick="play(\'' + noteId + '\');"' +
        ' class="my-btn audio bn" title="play audio" data-toggle="modal" ' +
        ' data-target="#' + noteId + '-modal">&nbsp</button>');
      modal.innerHTML = '<div class="modal-dialog modal-dialog-centered"><div' +
        ' class="modal-content"><button type="button" class="close" data-dismiss="modal">' +
        '&times;</button><audio controls id="' + noteId + '-media"' +
        ' src="#"></audio></div></div>';
      break;
    case 2:
    case "2":
      $(newNote).next().replaceWith('<button onclick="play(\'' + noteId + '\');" ' +
        'class="my-btn image bn" title="display picture" data-toggle="modal" ' +
        'data-target="#' + noteId + '-modal">&nbsp</button>');
      modal.innerHTML = '<div class="modal-dialog modal-dialog-centered"><div' +
        ' class="modal-content"><button type="button" class="close" data-dismiss="modal"' +
        '>&times;</button><img id="' + noteId + '-media" src="#" alt="image"' +
        ' /></div></div>';
      break;
    case 3:
    case "3":
      $(newNote).next().replaceWith('<button onclick="play(\'' + noteId + '\');"' +
        ' class="my-btn clip bn" title="show clip" data-toggle="modal" data-target="#' +
        noteId + '-modal">&nbsp</button>');
      modal.innerHTML = '<div class="modal-dialog modal-dialog-centered"><div' +
        ' class="modal-content"><button type="button" class="close" data-dismiss="modal">' +
        '&times;</button><video controls id="' + noteId + '-media"' +
        ' src="#"></video></div></div>';
      break;
  }
  newNote.parentElement.appendChild(modal);
}

/** Play audio, video or show picture
 * @param {String} noteId - id of the note element
 */
function play(noteId) {
  const note = document.getElementById(noteId + '-media');
  // if the note element has no source yet - load corresponding file
  if (note.getAttribute('src') === '#') {
    loadMediaFile(noteId);
    note.setAttribute("autoplay", "true");
  }
  // if it's an audio or video clip - make it play automatically
  if (note.tagName !== 'IMG') {
    note.play();
    // pause and rewind media on modal close
    $("#" + noteId + "-modal").on('hide.bs.modal', function () {
      note.pause();
      note.currentTime = 0;
    });
  }
}

/** Assigns media file to a media note when workout is loaded from database. !!!
 * @param {String} noteId - id of the note element
 * @param {String} filename - name of the file
 */
function assignFileToExistingMediaNote(noteId, filename) {
  for (let i = 0; i < filesToUpload.length; i++) {
    if (filesToUpload[i].name === filename) {
      $('#' + noteId + '-media').attr('src', URL.createObjectURL(filesToUpload[i]));
    }
  }
}

/** Puts the media file on files to remove list when a corresponding media note has been deleted.
 * @param {String} filename - name of the file
 */
function removeMediaFile(filename) {
  for (let i = 0; i < filesToUpload.length; i++) {
    if (filesToUpload[i].name === filename) {
      filesToUpload.splice(i, 1);
    }
  }
  const indexToRemove = workout.filenames.indexOf(filename);
  if (indexToRemove !== -1) {
    workout.filenames.splice(indexToRemove, 1);
    filesToRemove.push(filename);
  }
}

/** Removes element from workout and corresponding entry in workout object. Used to remove
 *  exercise, set or note of any kind from workout
 *  @param {Node} [element] - element to remove
 */
function remove(element) {
  if (confirm("Are you sure?") === true) {
    const entry = $(element).data('set').split(',');
    const parent = element.parentElement;
    parent.parentElement.removeChild(parent);
    let short = '';
    // noinspection JSMismatchedCollectionQueryUpdate
    let shorter = [];
    // Set parameters depending on how many levels deep element is positioned
    switch (entry.length) {
      case 3:
        short = workout[entry[0]][entry[1]];
        shorter = workout[entry[0]];
        break;
      case 5:
        short = workout[entry[0]][entry[1]][entry[2]][entry[3]];
        shorter = workout[entry[0]][entry[1]][entry[2]];
        break;
      case 7:
        short = workout[entry[0]][entry[1]][entry[2]][entry[3]][entry[4]][entry[5]];
        shorter = workout[entry[0]][entry[1]][entry[2]][entry[3]][entry[4]];
        break;
    }
    // If it's a media note - remove corresponding media file
    if (short.hasOwnProperty('type') && short.type > 0) {
      removeMediaFile(short.content);
    }
    // Remove element, clear workout screen and display workout again
    shorter.splice(entry[entry.length - 2], 1);
    document.getElementById('notes').innerHTML = '';
    document.getElementById('exercises').innerHTML = '';
    displayWorkout();
  }
}

/*****************************************************
 * Functions and variables related to I/O operations *
 *****************************************************/

/** Stores file attached to a media note in files array, removes input field, shows filename
 *  and play button.
 * @param {string} fileInputId - file input field Id
 * @param {File} file - file to upload
 * @param {number} noteType - type of note (text(0), audio(1), picture(2), video(3))
 */
function attachFile(fileInputId, file, noteType) {
  const filename = file.name;
  let filenameExists = (workout.filenames.indexOf(filename) !== -1);
  if (!filenameExists) {
    for (let i = 0; i < filesToUpload.length; i++) {
      if (filesToUpload[i].name === filename) {
        filenameExists = true;
        break;
      }
    }
  }
  if (filenameExists) {
    alert('Filename already exists!');
    document.getElementById(fileInputId).value = null;
    return;
  }
  displayExistingMediaNote(fileInputId, filename, noteType);
  filesToUpload.push(file);
  $('#' + fileInputId + '-media')
    .attr('src', URL.createObjectURL(filesToUpload[filesToUpload.length - 1]));
}

/** Gets existing workout details in JSON format from REST controller.
 * @param {number} [workoutId] - id number of workout to be loaded from database */
function loadWorkout(workoutId) {
  if (okToIgnoreChanges() === false) { return false; }
  const csrfToken = $("meta[name='_csrf']").attr("content");
  $.ajax({
    url: BASE_URL + 'workout/' + workoutId,
    headers: {"X-CSRF-TOKEN": csrfToken},
    type: 'GET',
    dataType: 'JSON',
    async: true,
  }).then(function (data) {
    sessionStorage.setItem('workout', JSON.stringify(data));
    storeLogoState();
    window.location.href = BASE_URL + 'workout/';
  }, function () {
    alert('There\'s been a problem loading the workout data!');
  });
  return true;
}

/** Checks whether user wants to ignore unsaved changes if there are any
 * @returns {boolean} true
 */
function okToIgnoreChanges() {
  if (JSON.stringify(workout) !== JSON.stringify(originalWorkout)) {
    console.log(workout);
    console.log(originalWorkout);
    if (confirm("You have unsaved changes!") === false) { return false; }
  }
  storeLogoState();
  return true;
}

/** Sends new workout object in JSON format to REST controller using POST AJAX call.
 * @param {object} [workout] - workout object to be persisted */
function saveWorkout(workout) {
  // Update the "updated" workout entry with current timestamp
  workout['updated'] = new Date().toISOString().slice(0, 19).replace('T', ' ');
  // Save workout
  const csrfToken = $("meta[name='_csrf']").attr("content");
  const formData = new FormData();
  formData.append('workout',
    new Blob([JSON.stringify(workout)], {type: 'application/json'}));
  formData.append('filesToRemove',
    new Blob([JSON.stringify(filesToRemove)], {type: 'application/json'}));
  filesToUpload.forEach(element => formData.append('filesToUpload', element));
  $.ajax({
    url: window.location.href,
    headers: {"X-CSRF-TOKEN": csrfToken},
    type: 'POST',
    data: formData,
    enctype: 'multipart/form-data',
    processData: false,
    contentType: false,
    cache: false,
    async: true,
  }).then(function () {
    storeLogoState();
    window.location.href = BASE_URL + 'user';
  }, function () {
    alert('There\'s been a problem saving the workout data!');
  });
}

/** Request deletion of the currently displayed workout from the database. */
function deleteWorkout() {
  if (confirm("Are you sure you want to delete this workout?") === true) {
    const csrfToken = $("meta[name='_csrf']").attr("content");
    $.ajax({
      url: BASE_URL + 'workout/' + workout.id,
      headers: {"X-CSRF-TOKEN": csrfToken},
      type: 'DELETE',
      async: true,
    }).then(function () {
      storeLogoState();
      window.location.href = BASE_URL + 'user';
    }, function () {
      alert('There\'s been a problem deleting this workout!');
    });
  }
}

/** Request a media file from controller.
 * @param {String} noteId - id of the note element associated with this file.
 */
function loadMediaFile(noteId) {
  const csrfToken = $("meta[name='_csrf']").attr("content");
  const filename = document.getElementById(noteId).innerHTML;
  $.ajax({
    url: BASE_URL + 'workout/file/' + workout.id + '/' + filename,
    headers: {"X-CSRF-TOKEN": csrfToken},
    type: 'GET',
    async: true,
  }).then(function (data, status, xhr) {
    // create base64 string and set it as a source for note element
    let base64source = 'data:' + xhr.getResponseHeader('type') + ';base64,' + data;
    $('#' + noteId + '-media').attr('src', base64source);
  }, function () {
    alert('There\'s been a problem loading this file!');
  });
}