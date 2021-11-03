

function makeDraggable(elements){

    for (let i = elements.length - 1; i >= 0; i--) {
        doDraggable(elements[i]);
    }

    let startE;

    function doDraggable(element){
        element.draggable = true;
        element.addEventListener("dragstart", dragStart); //save dragged element reference
        element.addEventListener("dragover", dragOver); // change color of reference element to red
        element.addEventListener("dragleave", dragLeave); // change color of reference element to black
        element.addEventListener("drop", drop); //change position of dragged element using the referenced element
    }
    /*
    The dragstart event is fired when the user starts
    dragging an element (if it is draggable=True)
    */
    function dragStart(event){
        startE=event.target.closest("li")
    }

    /*
        The dragover event is fired when an element
        is being dragged over a valid drop target.
     */
    function dragOver(event){
        event.preventDefault();

        var dest= event.target.closest("li");
        dest.className="selected";
    }

    /*
        The dragleave event is fired when a dragged
        element leaves a valid drop target.
    */
    function dragLeave(event){
        var dest = event.target.closest("li");

        // Mark  the current element as "notselected", then with CSS we will put it in black
        dest.className = "notselected";
    }

    function updateTree(start_id, dest_id) {
        var dest= document.getElementById(dest_id);
        let element=dest;
        for(var i=1; i<10 && element!=null; i++){
            let idx=dest_id*10+i;
            element=document.getElementById(idx);
        }
        var newId=dest_id*10+(i-1);
        var ul= document.createElement("ul");
        var li= document.createElement("li")
        li.textContent=newId+"."+document.getElementById(start_id).textContent.split('.')[1];
        li.id=newId;
        doDraggable(li);
        ul.appendChild(li)
        dest.appendChild(ul);
        document.getElementById(start_id).closest("ul").innerHTML='';
    }

    /*
        The drop event is fired when an element or text selection is dropped on a valid drop target.
    */
    function drop(event){
        event.stopImmediatePropagation();
        var dest = event.target.closest("li");
        var txt= "Do you wanna move topic "+ startE.getAttribute("id") + " into topic "+ dest.getAttribute("id")+ "?";

        if(confirm(txt)){
            reset(dest);
            updateTree(startE.getAttribute("id"), dest.getAttribute("id"));
        }
        else{
            reset(dest);
        }

    }

    function reset(element){
        element.className="notselected";
    }


}