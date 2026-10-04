// Registration page: preview the chosen photo and flag files over 2 MB before upload.
(function () {
  const MAX_BYTES = 2 * 1024 * 1024;
  const input = document.getElementById('photo');
  const preview = document.getElementById('photo-preview');
  const note = document.getElementById('photo-note');
  if (!input || !preview || !note) return;

  const defaultNote = note.textContent;
  let objectUrl = null;

  input.addEventListener('change', function () {
    const file = input.files && input.files[0];
    if (objectUrl) URL.revokeObjectURL(objectUrl);
    preview.hidden = true;
    note.classList.remove('error-text');
    input.removeAttribute('aria-invalid');
    note.textContent = defaultNote;

    if (!file) return;
    if (file.size > MAX_BYTES) {
      note.textContent = 'This photo is larger than 2 MB. Choose a smaller one.';
      note.classList.add('error-text');
      input.setAttribute('aria-invalid', 'true');
      return;
    }
    objectUrl = URL.createObjectURL(file);
    preview.src = objectUrl;
    preview.hidden = false;
  });
})();
