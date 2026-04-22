document.addEventListener("DOMContentLoaded",()=>{
    const loginForm = document.getElementById("form")
    const host = window.location.origin
    console.log(loginForm)
    loginForm.addEventListener("submit",(e)=>{
        e.preventDefault()
        const form = new FormData(loginForm)
        const data = Object.fromEntries(form.entries())
        fetch(`${host}/auth/login`,{
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
            window.location.href =`${host}/`
        })).catch(err=>console.error(err))
    })
})