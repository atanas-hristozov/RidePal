// Get all checkbox elements
const checkboxes = document.querySelectorAll('.checkbox-input');

// Loop through each checkbox element
checkboxes.forEach(checkbox => {
    // Add click event listener to each checkbox
    checkbox.addEventListener('click', function(event) {
        // Get the checkbox tile element for the clicked checkbox
        const checkboxTile = this.parentNode.querySelector('.checkbox-tile');

        // Toggle the border color on click
        checkboxTile.style.borderColor = this.checked ? '#00A2FF' : '#000';
        checkboxTile.style.boxShadow = this.checked ? '0 0 10px #00A2FF' : 'none';
    });
});