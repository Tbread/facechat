/*
* 나중에 헤더로 따로빼기?
* */

function logout(){
    fetch("/user/logout",{
        method:'GET'}).then(
           res =>{
               window.location.href="/"
           })
}