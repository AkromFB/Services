document.addEventListener("DOMContentLoaded",()=>{
    const registerForm = document.getElementById("form")
    const host = "http://"+window.location.host
    console.log(registerForm)
    registerForm.addEventListener("submit",(e)=>{
        e.preventDefault()
        const form = new FormData(registerForm)
        const data = Object.fromEntries(form.entries())
        fetch(`${host}/auth/register`,{
            method:"POST",
            headers:{
                "Content-Type":"application/json"
            },
            body:JSON.stringify(data)
        }).then(res=>res.json().then(final=>{
            if(!res.ok){
                throw Error(final.message)
            }
            return final
        }).then(_=>{
            window.location.href =`${host}/login`
        })).catch(err=>console.error(err))
    })
})