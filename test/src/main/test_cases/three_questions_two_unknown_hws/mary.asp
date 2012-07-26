world {
	said(youLied).
	said(youOwe).
	said(husbandWifeScandal).
	argued(john).
} view->Coroner {
	said(husbandWifeScandal).
	argued(john) :- said(husbandWifeScandal), not argued(alfred).
	argued(alfred) :- said(husbandWifeScandal), not argued(john).
	argued(john) :- said(youOwe), not argued(alfred), not argued(lawrence).
	argued(alfred) :- said(youOwe), not argued(john), not argued(lawrence).
	argued(lawrence) :- said(youOwe), not argued(john), not argued(alfred).
	
}
