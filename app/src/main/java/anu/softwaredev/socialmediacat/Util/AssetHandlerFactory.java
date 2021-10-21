package anu.softwaredev.socialmediacat.Util;

/** Factory for objects in handling local data instances */
public class AssetHandlerFactory {

    public AssetHandler createHandler(String type) {

        switch (type) {
            case "csv":
                return new CSVHandler();
            case "txt":
                return new BespokeHandler();
            case "json":
                return new JsonHandler();
            case "dummy":
                return new DummyHandler();        // for quick tests
        }

        return null;    // if unmatched



    }


}
