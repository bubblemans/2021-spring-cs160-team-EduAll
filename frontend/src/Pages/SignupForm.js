import React, { useContext, useState, useEffect } from 'react'
import { ContextStore } from '../ContextStore';
import { useHistory } from "react-router-dom";
import Select from "react-select";

export default function SignupForm() {
    const [formDetails, setFormDetails] = useState({email: "", password : "", firstName : "", lastName: ""})
    const [userType, setUserType] = useState()
    const { CurrentUser, SideBar } = useContext(ContextStore);
    const [ user, setUser ] = CurrentUser
    const [ showSidebar, setShowSidebar] = SideBar

    const history = useHistory()
    useEffect(() => {
        setShowSidebar(false)
    },[])

    const submitHandler = e => {
        e.preventDefault();
        verifyDetails()
    }

    const userTypes = [
        { value: "Student", label: "Student" },
        { value: "Professor", label: "Professor" },
      ];

    const verifyDetails = () => {
        let email = "admin@admin.com"
        let password = "admin123"

        if(formDetails.email != email && formDetails.password != password){
              console.log("Registered. Now proceed to log in");
              setUser(formDetails.name)
              // Push to the Home Page 
              history.push('/')

        }else {
            console.log("Sorry, user already exists.");
        }


    }
    return (
        <form onSubmit = {submitHandler}>
            <div className = "form-inner">
                <h2>EduALL</h2>
                <div className = "form-group">

                    <label htmlFor ="firstName">First name: </label>
                    <input type = "firstName" name = "firstName" id = "firstName"  onChange = {e => setFormDetails({...formDetails,firstName: e.target.value})} value = {formDetails.firstName}/>
                    <label htmlFor ="lastName">Last name: </label>
                    <input type = "lastName" name = "lastName" id = "lastName"  onChange = {e => setFormDetails({...formDetails,lastName: e.target.value})} value = {formDetails.lastName}/>
                
                    <label htmlFor ="email">Email: </label>
                    <input type = "email" name = "email" id = "email"  onChange = {e => setFormDetails({...formDetails,email: e.target.value})} value = {formDetails.email}/>
                    <label htmlFor ="password">Password: </label>
                    <input type = "password" name = "password" id = "password" onChange = {e => setFormDetails({...formDetails,password: e.target.value})} value = {formDetails.password}/>
                    <br/>
                    <Select options={userTypes} value={userType} defaultValue = {userType} onChange = {setUserType}  placeholder="Select your type..."/>                
                </div>
                <input type = "submit" value = "Register"/>
            </div>
        </form>
    )
}