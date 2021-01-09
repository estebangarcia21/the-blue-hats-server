WORKING_DIRECTORY="../terraform"

if [[ "$1" == init ]]; then
    cd $WORKING_DIRECTORY || exit

    terraform init
fi

if [[ "$1" == plan ]]; then
    cd $WORKING_DIRECTORY || exit

    terraform plan \
    -var-file="secrets.tfvars" \
    -out="tfplan"
fi

if [[ "$1" == apply ]]; then
    cd $WORKING_DIRECTORY || exit

    terraform apply \
    tfplan
fi

if [[ "$1" == "" ]]; then
    echo "
    Terraform Wrapper by Im-Stevemmmmm

    Commands      Description
    -----------------------------------------------------
    init          Initializes the providers
    plan          Creates a plan and outputs as tfplan
    apply         Applies tfplan
    "
fi
