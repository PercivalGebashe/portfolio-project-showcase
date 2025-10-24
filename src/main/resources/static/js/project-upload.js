function addParagraph() {
    const container = document.getElementById('contentContainer');

    const wrapper = document.createElement('div');
    wrapper.classList.add('dynamic-card');

    const typeInput = document.createElement('input');
    typeInput.type = "hidden";
    typeInput.name = "content[].type";
    typeInput.value = "paragraph";

    const valueInput = document.createElement('textarea');
    valueInput.name = "content[].value";
    valueInput.placeholder = "Paragraph text...";
    valueInput.rows = 3;

    const removeBtn = document.createElement('button');
    removeBtn.type = "button";
    removeBtn.innerText = "Remove";
    removeBtn.classList.add('remove-btn');
    removeBtn.onclick = () => wrapper.remove();

    wrapper.appendChild(typeInput);
    wrapper.appendChild(valueInput);
    wrapper.appendChild(removeBtn);
    container.appendChild(wrapper);
}

function addScreenshot() {
    const container = document.getElementById('contentContainer');

    const wrapper = document.createElement('div');
    wrapper.classList.add('dynamic-card');

    const valueInput = document.createElement('input');
    valueInput.type = "file"
    valueInput.name = "image";
    valueInput.accepts = "image/pgn, image/jpg";

    const removeBtn = document.createElement('button');
    removeBtn.type = "button";
    removeBtn.innerText = "Remove";
    removeBtn.classList.add('remove-btn');
    removeBtn.onclick = () => wrapper.remove();

    wrapper.appendChild(valueInput);
    wrapper.appendChild(removeBtn);
    container.appendChild(wrapper);
}