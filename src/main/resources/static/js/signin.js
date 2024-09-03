function signin() {
    let username = document.getElementById('username').value;
    let password = document.getElementById('password').value;
    if (!username) {
        alert('아이디를 입력해주세요.')
        return
    }
    if (!password) {
        alert('비밀번호를 입력해주세요.')
        return
    }
    postSignIn({username: username, password: password})
}

function postSignIn(req) {
    fetch("/user/sign_in", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(req)
    }).then(res => res.json()).then(data => {
        if (data) {
            if (!data.success){
                alert(data.message)
                return
            }
            window.location.href="/account/sign_in"
        }
    })
}