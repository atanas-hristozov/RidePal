document.getElementById('imageInput').addEventListener('change', function(event) {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.onload = function(e) {
      document.getElementById('previewImage').setAttribute('src', e.target.result);
    }

    reader.readAsDataURL(file);
  });