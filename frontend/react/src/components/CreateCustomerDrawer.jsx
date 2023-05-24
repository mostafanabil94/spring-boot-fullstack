import {
  Button,
  Drawer,
  DrawerBody,
  DrawerCloseButton,
  DrawerContent,
  DrawerFooter,
  DrawerHeader,
  DrawerOverlay,
  useDisclosure,
} from "@chakra-ui/react";
import CreateCustomerForm from "./CreateCustomerForm";

const AddIcon = () => "+";
const CloseIcon = () => "x";

const CreateCustomerDrawer = ({ fetchCustomers }) => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  return (
    <>
      <Button leftIcon={<AddIcon />} colorScheme={"teal"} onClick={onOpen}>
        Create Customer
      </Button>

      <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
        <DrawerOverlay />
        <DrawerContent>
          <DrawerCloseButton />
          <DrawerHeader>Create Customer</DrawerHeader>

          <DrawerBody>
            <CreateCustomerForm fetchCustomers={fetchCustomers} />
          </DrawerBody>

          <DrawerFooter>
            <Button
              leftIcon={<CloseIcon />}
              colorScheme={"teal"}
              onClick={onClose}
            >
              Close
            </Button>
          </DrawerFooter>
        </DrawerContent>
      </Drawer>
    </>
  );
};

export default CreateCustomerDrawer;
