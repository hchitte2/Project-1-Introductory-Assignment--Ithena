// Registration page: preview the chosen photo, and stop the form from uploading a file over 2 MB.
(function () {
  const MAX_BYTES = 2 * 1024 * 1024;
  const TOO_LARGE = 'This photo is larger than 2 MB. Choose a smaller one.';
  const input = document.getElementById('photo');
  const preview = document.getElementById('photo-preview');
  const note = document.getElementById('photo-note');
  if (!input || !preview || !note) return;

  const defaultNote = note.textContent;
  let objectUrl = null;

  function chosenFile() {
    return input.files && input.files[0];
  }

  function showTooLarge() {
    note.textContent = TOO_LARGE;
    note.classList.add('error-text');
    input.setAttribute('aria-invalid', 'true');
  }

  input.addEventListener('change', function () {
    const file = chosenFile();
    if (objectUrl) URL.revokeObjectURL(objectUrl);
    preview.hidden = true;
    note.classList.remove('error-text');
    input.removeAttribute('aria-invalid');
    note.textContent = defaultNote;

    if (!file) return;
    if (file.size > MAX_BYTES) {
      showTooLarge();
      return;
    }
    objectUrl = URL.createObjectURL(file);
    preview.src = objectUrl;
    preview.hidden = false;
  });

  input.form.addEventListener('submit', function (event) {
    const file = chosenFile();
    if (file && file.size > MAX_BYTES) {
      event.preventDefault();
      showTooLarge();
      input.focus();
    }
  });
})();
