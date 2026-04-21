document.addEventListener("DOMContentLoaded",()=>{
    const toggleButton= document.getElementById("toggle-btn");
    const toggleMenu = document.getElementById("menu")
    toggleButton.addEventListener("click",()=>{
        toggleMenu.classList.toggle("open");
    })
})