let paragraphCount = 0;

// Add new paragraph section
function addParagraph() {
    const container = document.getElementById('contentContainer');
    paragraphCount++;

    const wrapper = document.createElement('div');
    wrapper.classList.add('dynamic-card');
    wrapper.dataset.paragraphId = `p${paragraphCount}`;

    const typeInput = document.createElement('input');
    typeInput.type = "hidden";
    typeInput.name = `content[${paragraphCount - 1}].type`;
    typeInput.value = "paragraph";

    const valueInput = document.createElement('textarea');
    valueInput.name = `content[${paragraphCount - 1}].text`;
    valueInput.placeholder = `Write paragraph ${paragraphCount}...`;
    valueInput.required = true;

    const addScreenshotBtn = document.createElement('button');
    addScreenshotBtn.type = "button";
    addScreenshotBtn.classList.add('add-btn');
    addScreenshotBtn.textContent = "+ Add Screenshot";
    addScreenshotBtn.onclick = () => addScreenshot(wrapper);

    const fileInput = document.createElement('input');
    fileInput.type = "file";
    fileInput.accept = "image/png, image/jpeg";
    fileInput.style.display = "none";
    fileInput.name = "images";
    fileInput.onchange = (e) => handleFileSelection(e, wrapper);

    const removeBtn = document.createElement('button');
    removeBtn.type = "button";
    removeBtn.classList.add('remove-btn');
    removeBtn.textContent = "Remove Paragraph";
    removeBtn.onclick = () => wrapper.remove();

    wrapper.append(typeInput, valueInput, addScreenshotBtn, fileInput, removeBtn);
    container.appendChild(wrapper);
}

// Screenshot handling
function addScreenshot(wrapper) {
    const fileInput = wrapper.querySelector('input[type="file"]');
    fileInput.click();
}

function handleFileSelection(event, wrapper) {
    const file = event.target.files[0];
    if (!file) return;

    const paragraphId = wrapper.dataset.paragraphId;
    const ext = file.name.split('.').pop();
    const newFileName = `${paragraphId}_screen.${ext}`;
    const renamedFile = new File([file], newFileName, { type: file.type });

    const dataTransfer = new DataTransfer();
    dataTransfer.items.add(renamedFile);
    event.target.files = dataTransfer.files;

    const existingPreview = wrapper.querySelector('.preview');
    if (existingPreview) existingPreview.remove();

    const preview = document.createElement('div');
    preview.classList.add('preview');

    const img = document.createElement('img');
    img.src = URL.createObjectURL(renamedFile);

    const caption = document.createElement('p');
    caption.textContent = newFileName;
    caption.style.color = "var(--muted)";
    caption.style.fontSize = "0.85rem";

    preview.append(img, caption);
    wrapper.appendChild(preview);
}

/* ===============================
   TECHNOLOGIES INPUT HANDLING
=============================== */
const techInput = document.getElementById('techInput');
const techList = document.getElementById('techList');
const hiddenField = document.getElementById('technologiesField');

let technologies = [];

techInput.addEventListener('keydown', (e) => {
    if (e.key === 'Enter' || e.key === ',') {
        e.preventDefault();
        const value = techInput.value.trim().replace(/,$/, '');
        if (value && !technologies.includes(value)) {
            technologies.push(value);
            renderTechList();
        }
        techInput.value = '';
    }
});

function renderTechList() {
    techList.innerHTML = '';
    technologies.forEach(tech => {
        const tag = document.createElement('span');
        tag.textContent = tech;
        tag.onclick = () => {
            technologies = technologies.filter(t => t !== tech);
            renderTechList();
        };
        techList.appendChild(tag);
    });
    hiddenField.value = JSON.stringify(technologies);
}
