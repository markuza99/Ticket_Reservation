const validatePrice = (...prices) => {
	let validation = true;
	prices.forEach(elem => {
		if(elem == "") return;
		if ((isNaN(elem) || elem > 100000 || elem < 0)) {
		
			validation = false;
		}
	});
	return validation;
}

const validateNumberRange = (from, to, number) => {
    return (number >= from && number <= to);
}

const validateRange = (from, to) => {
	return !(to < from);
}


const isEmpty = (elem) => {
    return (elem == null || elem == undefined || elem =="") 
}

const checkInputFields = (fields) => {
    if(areInputFieldsEmpty(fields)) {
    	return "Morate popuniti sva polja.";
    }

    if(!isPasswordStrongEnough(fields.lozinka)) {
       return "Password nije dobar.";
    }

    return true;
}

const isFieldEmpty = (field) => {
    return field == "" ? true : false;
}

const areInputFieldsEmpty = (fields) => {

    for (const [key, value] of Object.entries(fields)) {
        if(isFieldEmpty(value)) 
            return true;
    }

    return false; // ne, nije prazan
}

const usernameExists = (username) => {
    if(username == "pera")
        return true;
    return false;
}

const validateEmail = (email) => {
    if(email == "pera") {
        return false;
    }
    return true;
}

const isPasswordStrongEnough = (password) => {
    if(password.length < 5 || password.length > 40) {
    
        return false;
    }
    return true;
}